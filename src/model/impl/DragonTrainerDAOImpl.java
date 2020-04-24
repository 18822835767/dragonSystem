package model.impl;

import entity.DragonTrainer;
import model.IDragonTrainerDAO;
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
 * */
public class DragonTrainerDAOImpl implements IDragonTrainerDAO {
    private volatile static DragonTrainerDAOImpl instance = null;

    private DragonTrainerDAOImpl(){}

    public static DragonTrainerDAOImpl getInstance(){
        if(instance == null){
            synchronized (DragonTrainerDAOImpl.class){
                if(instance == null){
                    instance = new DragonTrainerDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(int dragonGroupId, String name, String username, String password) {
        String sql = "insert into dragontrainer(dragonGroupId,name,username,password) values(?,?,?,?)";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,dragonGroupId,name,username, Encrypt.setEncrypt(password));
    }

    @Override
    public int delete(int dragonTrainerId) {
        String sql = "delete from dragontrainer where dragonTrainerId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql, dragonTrainerId);
    }

    @Override
    public int update(int id, int dragonGroupId, String name, String password) {
        String sql = "update dragontrainer set dragonGroupId=?,name=?,password=? where dragonTrainerId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,dragonGroupId,name,Encrypt.setEncrypt(password),id);
    }

    /**
     * id查询.
     * */
    @Override
    public DragonTrainer get(int dragonTrainerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragontrainer where dragonTrainerId = ?";
            if(conn != null){
                ps = conn.prepareStatement(sql);
                ps.setInt(1,dragonTrainerId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                            , rs.getString("name"), rs.getString("username"),
                            Encrypt.getEncrypt( rs.getString("password")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    /**
     * 用户名和密码查询.
     * */
    @Override
    public DragonTrainer get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragontrainer where username = ? and password = ?";
            if(conn != null){
                ps = conn.prepareStatement(sql);
                ps.setString(1,username);
                ps.setString(2,Encrypt.setEncrypt(password));
                rs = ps.executeQuery();
                if(rs.next()){
                    return new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                            , rs.getString("name"), rs.getString("username"),Encrypt.getEncrypt(
                            rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }

    /**
     * 找到所有的驯龙高手。
     * */
    @Override
    public List<DragonTrainer> getList() {
        List<DragonTrainer> dragonTrainerList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragontrainer";
        try {
            conn = DBUtils.getConnection();
            if(conn != null){
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    DragonTrainer dragonTrainer = new DragonTrainer(rs.getInt("dragonTrainerId"),
                            rs.getInt("dragonGroupId"), rs.getString("name"), rs.getString("username"),
                            Encrypt.getEncrypt(rs.getString("password")));
                    dragonTrainerList.add(dragonTrainer);
                }
            }
            return dragonTrainerList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

}
