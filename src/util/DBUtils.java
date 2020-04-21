package util;

import model.database.ConnectionProxy;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC工具包
 */
public class DBUtils {
    /**
     * 初始化一个连接池对象.
     * */
    private static DataSource dataSource = new ConnectionProxy();

    private DBUtils() {
    }

    /**
     * 直接从连接池里拿.
     * */
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
    public static int executeUpdate(Connection conn,String sql, Object... params) {
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
            DBUtils.close(conn, ps, null);
        }
        return 0;
    }


}
