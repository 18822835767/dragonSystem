package model.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ConnectionProxy extends DataSourceAdapter {
    private static int initConnections = 5;
    private static int maxConnections = 10;//空闲池允许的最大的连接数量
    private static LinkedList<Connection> freePools = new LinkedList<>();//空闲的连接池
    private static LinkedList<Connection> activePools = new LinkedList<>();//使用中的连接池
    private static int maxTotalConnections = 30;//允许的最大的连接总数.
    private static int count = 0;//记录已开启的连接

    boolean end = false;//判断程序是否结束。

    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("resource/jdbc");
            String driver = bundle.getString("driver");
            String url = bundle.getString("url");
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            Class.forName(driver);
            //初始化连接池的数量
            for (int i = 0; i < initConnections; i++) {
                freePools.addFirst(DriverManager.getConnection(url, username, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = freePools.removeLast();
        //返回代理
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            freePools.addFirst(conn);
                        } else {
                            return method.invoke(conn, args);
                        }
                        return null;
                    }
                });
    }
}
