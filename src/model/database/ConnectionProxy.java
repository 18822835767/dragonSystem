package model.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ConnectionProxy extends DataSourceAdapter{
    private static int initConnections = 5;
    private static int poolMaxIdleConnections = 10;//空闲池允许的最大的连接数量
    private static int poolMaxActiveConnections = 10;
    private static LinkedList<Connection> idleConnections = new LinkedList<>();//空闲的连接池
    private static LinkedList<Connection> activeConnections = new LinkedList<>();//激活的连接池

    private final Object monitor = new Object();//用于同步

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
                idleConnections.addFirst(DriverManager.getConnection(url, username, password));
            }
            System.out.println("初始化后空闲连接池:"+idleConnections.size()+"  激活连接池:"+activeConnections.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = null;

        while(conn == null){
            synchronized (monitor){
                //如果空闲连接池不为空，直接获取连接
                if(!idleConnections.isEmpty()){
                    conn = idleConnections.remove(0);
                }else{
                    //没有空闲连接可以使用，那么我们需要获取新的连接(也就是需要传建一个连接)
                    if(activeConnections.size() < poolMaxActiveConnections){
                        //如果当前激活的连接数 小于 我们允许的最大连接数，那么此时可以创建一个新的连接，否则还不能创建
                        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dragonsystem","root",
                                "333");
                    }
                    //否则是不能创建新连接的，需要等待，wait
                }
            }

            if(conn == null){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //线程给打断则跳出循环
                    break;
                }
            }

        }

        if(conn != null){
            //对象不为空，说明已经拿到了该连接
            activeConnections.add(conn);
            System.out.println("得到连接后空闲连接池:"+idleConnections.size()+"  激活连接池:"+activeConnections.size());
        }
        //返回连接对象
        Connection finalConn = conn;
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            synchronized (monitor){
                                activeConnections.remove(finalConn);
                                if(idleConnections.size() < poolMaxIdleConnections){
                                    idleConnections.addFirst(finalConn);
                                }
                                monitor.notifyAll();

                                System.out.println("关闭连接后空闲连接池:"+idleConnections.size()+"  激活连接池:"+activeConnections.size());
                            }
                        } else {
                            return method.invoke(finalConn, args);
                        }
                        return null;
                    }
                });

//        Connection conn = idleConnections.removeLast();
//        //返回代理
//        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[]{Connection.class},
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                        if ("close".equals(method.getName())) {
//                            idleConnections.addFirst(conn);
//                        } else {
//                            return method.invoke(conn, args);
//                        }
//                        return null;
//                    }
//                });
    }

}
