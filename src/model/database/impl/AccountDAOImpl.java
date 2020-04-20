package model.database.impl;

import entity.Account;
import model.IAccountDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements IAccountDAO {
    private volatile static AccountDAOImpl instance = null;

    private AccountDAOImpl(){}

    public static AccountDAOImpl getInstance(){
        if(instance == null){
            synchronized (AccountDAOImpl.class){
                if(instance == null){
                    instance = new AccountDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(int foreignerId, double money, String createTime, String status) {
        String sql = "insert into account(foreignerId,money,createTime,status) values(?,?,?,?) ";
        return DBUtils.executeUpdate(sql,foreignerId,money,createTime,status);
    }

    @Override
    public List<Account> getForeignerListByStatus(int foreignerId, String status) {
        List<Account> accounts = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from account where foreignerId = ? and status = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,foreignerId);
            ps.setString(2,status);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("accountId"),rs.getInt("foreignerId"),rs.getFloat("money"),
                        rs.getString("createTime"),rs.getString("status"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public List<Account> getListById(int foreignerId) {
        List<Account> accounts = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from account where foreignerId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,foreignerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("accountId"),rs.getInt("foreignerId"),rs.getFloat("money"),
                        rs.getString("createTime"),rs.getString("status"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public List<Account> getAllListByStatus(String status) {
        List<Account> accounts = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from account where status = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,status);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("accountId"),rs.getInt("foreignerId"),rs.getFloat("money"),
                        rs.getString("createTime"),rs.getString("status"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public List<Account> getAllList() {
        List<Account> accounts = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from account";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("accountId"),rs.getInt("foreignerId"),rs.getFloat("money"),
                        rs.getString("createTime"),rs.getString("status"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }
}
