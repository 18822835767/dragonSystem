package model.database.impl;

import entity.Dragon;
import model.IDragonDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 因为该类频繁的需要用到，所以采取单例，节省资源.
 * */
public class DragonDAOImpl implements IDragonDAO {
    private volatile static DragonDAOImpl instance = null;

    private DragonDAOImpl(){}

    public static DragonDAOImpl getInstance(){
        if(instance == null){
            synchronized (DragonDAOImpl.class){
                if(instance == null){
                    instance = new DragonDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(int dragonGroupId, String name, String profile, boolean training, boolean healthy, String sex, int age) {
        int isTraining = training ? 1 : 0;
        int isHealthy = healthy ? 1 : 0;
        String sql = "insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(?,?,?,?,?,?,?)";
        return DBUtils.executeUpdate(sql, dragonGroupId,name,profile,isTraining,isHealthy,sex,age);
    }

    @Override
    public int delete(int dragonId,int dragonGroupId) {
        String sql = "delete from dragon where dragonId = ? and dragonGroupId = ?";
        return DBUtils.executeUpdate(sql, dragonId,dragonGroupId);
    }

    @Override
    public int update(int id,int dragonGroupId, String name, String profile, boolean training, boolean healthy, int age) {
        int isTraining = training ? 1 : 0;
        int isHealthy = healthy ? 1 : 0;
        String sql = "update dragon set name = ?,profile = ?,training = ?,healthy = ?,age = ? where dragonId = ?";
        return DBUtils.executeUpdate(sql, name,profile,isTraining,isHealthy,age,id);
    }

    @Override
    public Dragon get(int dragonId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon where dragonId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dragonId);
            rs = ps.executeQuery();
            if (rs.next()) {
                //boolean与int的值转换
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                return new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public Dragon get(int dragonId,int dragonGroupId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon where dragonId = ? and dragonGroupId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dragonId);
            ps.setInt(2,dragonGroupId);
            rs = ps.executeQuery();
            if (rs.next()) {
                //boolean与int的值转换
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                return new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    @Override
    public Dragon get(int dragonGroupId,String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon where dragonGroupId = ? and name =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dragonGroupId);
            ps.setString(2,name);
            rs = ps.executeQuery();
            if (rs.next()) {
                //boolean与int的值转换
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                return new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    //找到某个族群的龙
    @Override
    public List<Dragon> getList(int dragonGroupId) {
        List<Dragon> dragonList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon where dragonGroupId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,dragonGroupId);
            rs = ps.executeQuery();
            while (rs.next()) {
                //boolean与int的值转换,因为表和类中该字段的属性不同
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
                dragonList.add(dragon);
            }
            return dragonList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }

    //找到所有的龙
    @Override
    public List<Dragon> getList() {
        List<Dragon> dragonList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //boolean与int的值转换,因为表和类中该字段的属性不同
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
                dragonList.add(dragon);
            }
            return dragonList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null, ps, rs);
        }
        return null;
    }
}

