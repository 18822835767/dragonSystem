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
    @Override
    public int save(int dragonGroupId, String name, String username, String password) {
        String sql = "insert into dragontrainer(dragonGroupId,name,username,password) values(?,?,?,?)";
        return DBUtils.executeUpdate(sql,  dragonGroupId,name,username,password);
    }

    @Override
    public int delete(int dragonTrainerId) {
        String sql = "delete from dragontrainer where dragonTrainerId = ?";
        return DBUtils.executeUpdate(sql, dragonTrainerId);
    }

    @Override
    public int update(int id, int dragonGroupId, String name, String username, String password) {
        String sql = "update dragontrainer set dragonGroupId=?,name=?,username=?,password=? where dragonTrainerId = ?";
        return DBUtils.executeUpdate(sql,dragonGroupId,name,username,password,id);
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
            DBUtils.close(null,ps, rs);
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
            DBUtils.close(null,ps,rs);
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
            DBUtils.close(null,ps, rs);
        }
        return null;
    }

    @Override
    public List<String> getUserNameList() {
        List<String> UserNameList = new ArrayList<>();
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
                UserNameList.add(dragonTrainer.getUsername());
            }
            return UserNameList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps, rs);
        }
        return null;
    }
}
