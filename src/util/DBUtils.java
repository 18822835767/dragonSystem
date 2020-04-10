package util;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * JDBC工具包
 */
public class DBUtils {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    private DBUtils() {
    }

    /**
     * 负责驱动，只执行一次.
     * */
    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("resource/jdbc");
            driver = bundle.getString("driver");
            url = bundle.getString("url");
            username = bundle.getString("username");
            password = bundle.getString("password");
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
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

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML语句封装在工具类里.
     * */
    public static int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //加载驱动、获取连接
            conn = DBUtils.getConnection();
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
            DBUtils.close(conn, ps, null);
        }
        return 0;
    }


}
