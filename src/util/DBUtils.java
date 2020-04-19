package util;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * JDBC工具包
 */
public class DBUtils {
    /**
     * 在静态代码块中加载connection，只有一份，重复利用
     * */
    private static Connection conn;

    private DBUtils() {
    }

    /**
     * 负责驱动与连接，只执行一次.
     * */
    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("resource/jdbc");
            String driver = bundle.getString("driver");
            String url = bundle.getString("url");
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            Class.forName(driver);

            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void close(Connection connection, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML语句封装在工具类里.
     * */
    public static int executeUpdate(String sql, Object... params) {
        PreparedStatement ps = null;
        try {
            //获取数据库预编译操作对象
            ps = conn.prepareStatement(sql);
            //params参数遍历
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, null);
        }
        return 0;
    }


}
