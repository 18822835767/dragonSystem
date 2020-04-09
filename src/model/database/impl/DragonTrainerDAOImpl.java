package model.database.impl;

import entity.Dragon;
import entity.DragonGroup;
import entity.DragonTrainer;
import model.IDragonTrainerDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DragonTrainerDAOImpl implements IDragonTrainerDAO {
    //该方法可能没用
    @Override
    public void save(DragonTrainer dT) {
        String sql = "insert into dragontrainer(dragonTrainerId,dragonGroupId,name,username,password) values(?,?,?,?)";
        DBUtils.executeUpdate(sql, dT.getDragonTrainerId(), dT.getDragonGroupId(), dT.getName(), dT.getUsername(), dT.getPassword());
    }

    @Override
    public void save(int dragonGroupId, String name, String username, String password) {
        String sql = "insert into dragontrainer(dragonGroupId,name,username,password) values(?,?,?,?)";
        DBUtils.executeUpdate(sql,  dragonGroupId,name,username,password);
    }

    @Override
    public void delete(int dragonTrainerId) {
        String sql = "delete from dragontrainer where dragonTrainerId = ?";
        DBUtils.executeUpdate(sql, dragonTrainerId);
    }

    @Override//可能没用
    public void update(int dragonTrainerId, DragonTrainer dragonTrainer) {
        String sql = "update dragontrainer set dragonGroupId=?,name=?,username=?,password=? where dragonTrainerId = ?";
        DBUtils.executeUpdate(sql, dragonTrainer.getDragonGroupId(),dragonTrainer.getName(),dragonTrainer.getUsername(),
                dragonTrainer.getPassword(), dragonTrainerId);
    }

    @Override
    public void update(int id, int dragonGroupId, String name, String username, String password) {
        String sql = "update dragontrainer set dragonGroupId=?,name=?,username=?,password=? where dragonTrainerId = ?";
        DBUtils.executeUpdate(sql,dragonGroupId,name,username,password,id);
    }

    //id查询
    @Override
    public DragonTrainer get(int dragonTrainerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragontrainer where dragonTrainerId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,dragonTrainerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                DragonTrainer dragonTrainer = new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                        , rs.getString("name"), rs.getString("username"), rs.getString("password"));
                return dragonTrainer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    //用户名和密码查询
    @Override
    public DragonTrainer get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragontrainer where username = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            if(rs.next()){
                DragonTrainer dragonTrainer = new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                        , rs.getString("name"), rs.getString("username"), rs.getString("password"));
                return dragonTrainer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }

    //找到所有的驯龙高手
    @Override
    public List<DragonTrainer> getList() {
        List<DragonTrainer> dragonTrainerList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragontrainer";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DragonTrainer dragonTrainer = new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                        , rs.getString("name"), rs.getString("username"), rs.getString("password"));
                dragonTrainerList.add(dragonTrainer);
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
