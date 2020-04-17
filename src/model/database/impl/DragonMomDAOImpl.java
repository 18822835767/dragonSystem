package model.database.impl;

import entity.Dragon;
import entity.DragonMom;
import entity.DragonTrainer;
import model.IDragonMomDAO;
import util.DBUtils;
import util.Encrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 采取单例，节省资源.
 * */
public class DragonMomDAOImpl implements IDragonMomDAO {
    private volatile static DragonMomDAOImpl instance = null;

    private DragonMomDAOImpl(){}

    public static DragonMomDAOImpl getInstance(){
        if(instance == null){
            synchronized (DragonMomDAOImpl.class){
                if(instance == null){
                    instance = new DragonMomDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int update(double money) {
        double moneyTub = get().getMoneyTub();
        String sql = "update dragonmom set moneyTub = ?";
        return DBUtils.executeUpdate(sql,money);
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
                return new DragonMom(rs.getInt("dragonMomId"), rs.getString("name"),
                        rs.getString("username"), rs.getString("password"),rs.getFloat("moneyTub"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(null,ps,rs);
        }
        return null;
    }

    /**
     *用户名和密码查询,加密和解密.
     * */
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
            ps.setString(2, Encrypt.setEncrypt(password));
            rs = ps.executeQuery();
            if(rs.next()){
                return new DragonMom(rs.getInt("dragonMomId"), rs.getString("name"), rs.getString("username"),
                        Encrypt.getEncrypt(rs.getString("password")), rs.getFloat("moneyTub"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(null,ps,rs);
        }
        return null;
    }

}
