package model.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MyDataSource extends DataSourceAdapter {
    private static int size = 5;
    private static LinkedList<Connection> pools = new LinkedList<>();//连接池.

    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("resource/jdbc");
            String driver = bundle.getString("driver");
            String url = bundle.getString("url");
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            Class.forName(driver);
            //初始化连接池的数量
            for (int i = 0; i < size; i++) {
                pools.addFirst(DriverManager.getConnection(url, username, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = pools.removeLast();
        //返回代理
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            pools.addFirst(conn);
                        } else {
                            return method.invoke(conn, args);
                        }
                        return null;
                    }
                });
    }
}
