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

public class DragonDAOImpl implements IDragonDAO {
    @Override//可能没用
    public void save(Dragon d) {
        int training = d.isTraining() ? 1 : 0;
        int healthy = d.isHealthy() ? 1 : 0;
        String sql = "insert into dragon(dragonId,dragonGroupId,name,profile,training,healthy,sex,age) values(?,?,?,?,?,?,?,?)";
        DBUtils.executeUpdate(sql, d.getDragonId(), d.getDragonGroupId(), d.getName(), d.getProfile(), training, healthy, d.getSex(), d.getAge());
    }

    @Override
    public void save(int dragonGroupId, String name, String profile, boolean training, boolean healthy, String sex, int age) {
        int isTraining = training ? 1 : 0;
        int isHealthy = healthy ? 1 : 0;
        String sql = "insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(?,?,?,?,?,?,?)";
        DBUtils.executeUpdate(sql, dragonGroupId,name,profile,isTraining,isHealthy,sex,age);
    }

    @Override
    public void delete(int dragonId) {
        String sql = "delete from dragon where dragonId = ?";
        DBUtils.executeUpdate(sql, dragonId);
    }

    @Override
    public void updata(int dragonId, Dragon d) {
        int training = d.isTraining() ? 1 : 0;
        int healthy = d.isHealthy() ? 1 : 0;
        String sql = "update dragon set name = ?,profile = ?,training = ?,healthy = ?,sex = ?,age = ? where dragonId = ?";
        DBUtils.executeUpdate(sql, d.getName(), d.getProfile(), training, healthy, d.getSex(), d.getAge());
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
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
                return dragon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public Dragon get(int dragonId,String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragon where dragonId = ? and name =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dragonId);
            ps.setString(2,name);
            rs = ps.executeQuery();
            if (rs.next()) {
                //boolean与int的值转换
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, rs.getString("sex"), rs.getInt("age"));
                return dragon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
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
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }
}

