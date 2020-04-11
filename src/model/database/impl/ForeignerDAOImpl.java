package model.database.impl;

import entity.DragonMom;
import entity.DragonTrainer;
import entity.Foreigner;
import model.IForeignerDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForeignerDAOImpl implements IForeignerDAO {
    @Override
    public int save(String username, String password,String name) {
        String sql = "insert into foreigner(username,password,name) values(?,?,?)";
        return DBUtils.executeUpdate(sql,username,password,name);
    }

    @Override
    public int update(int foreignerId, double money) {
        String sql = "update foreigner set money = ? where foreignerId = ?";
        return DBUtils.executeUpdate(sql,money,foreignerId);
    }

    //用户名和密码查询
    @Override
    public Foreigner get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner where username = ? and password = ?";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            if(rs.next()){
                Foreigner foreigner = new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        rs.getString("password"),rs.getString("name"),rs.getInt("money"));
                return foreigner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }

    @Override
    public List<String> getUserNameList() {
        List<String> userNameList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
           while(rs.next()){
                Foreigner foreigner = new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        rs.getString("password"),rs.getString("name"),rs.getInt("money"));
                userNameList.add(foreigner.getUsername());
            }
           return userNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }
}
