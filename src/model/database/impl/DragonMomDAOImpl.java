package model.database.impl;

import entity.Dragon;
import entity.DragonMom;
import entity.DragonTrainer;
import model.IDragonMomDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DragonMomDAOImpl implements IDragonMomDAO {
    @Override
    public int update(double money) {
        double moneyTub = get().getMoneyTub();
        String sql = "update dragonmom set moneyTub = ?";
        return DBUtils.executeUpdate(sql,moneyTub+money);
    }

    @Override
    public DragonMom get() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragonmom";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                DragonMom dragonMom = new DragonMom(rs.getInt("dragonMomId"), rs.getString("name"),
                        rs.getString("username"), rs.getString("password"),rs.getFloat("moneyTub"));
                return dragonMom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }

    //用户名和密码查询
    @Override
    public DragonMom get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragonmom where username = ? and password = ?";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            if(rs.next()){
                DragonMom dragonMom = new DragonMom(rs.getInt("dragonMomId"), rs.getString("name"),
                        rs.getString("username"), rs.getString("password"),rs.getFloat("moneyTub"));
                return dragonMom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }

    @Override
    public String getUsername() {
        return get().getUsername();
    }
}
