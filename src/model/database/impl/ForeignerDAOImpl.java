package model.database.impl;

import entity.Foreigner;
import model.IForeignerDAO;
import util.DBUtils;
import util.Encrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 采取单例，节省资源.
 */
public class ForeignerDAOImpl implements IForeignerDAO {
    private volatile static ForeignerDAOImpl instance = null;

    private ForeignerDAOImpl() {
    }

    public static ForeignerDAOImpl getInstance() {
        if (instance == null) {
            synchronized (ForeignerDAOImpl.class) {
                if (instance == null) {
                    instance = new ForeignerDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(String username, String password, String name) {
        String sql = "insert into foreigner(username,password,name) values(?,?,?)";
        return DBUtils.executeUpdate(sql, username, Encrypt.setEncrypt(password), name);
    }

    @Override
    public int update(int foreignerId, double money) {
        String sql = "update foreigner set money = ? where foreignerId = ?";
        return DBUtils.executeUpdate(sql, money, foreignerId);
    }

    //用户名和密码查询
    @Override
    public Foreigner get(String username, String password) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner where username = ? and password = ?";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, Encrypt.setEncrypt(password));
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        Encrypt.getEncrypt(rs.getString("password")), rs.getString("name"),
                        rs.getFloat("money"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public Foreigner get(int foreignerId) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner where foreignerId = ?";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, foreignerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        Encrypt.getEncrypt(rs.getString("password")), rs.getString("name"), rs.getInt("money"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public List<Foreigner> getList() {
        List<Foreigner> foreigners = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Foreigner foreigner = new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        rs.getString("password"), rs.getString("name"), rs.getInt("money"));
                foreigners.add(foreigner);
            }
            return foreigners;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }
}
