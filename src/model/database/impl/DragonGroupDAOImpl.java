package model.database.impl;

import entity.Dragon;
import entity.DragonGroup;
import model.IDragonGroupDAO;
import util.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 采取单例，节省资源.
 * */
public class DragonGroupDAOImpl implements IDragonGroupDAO {
    private volatile static DragonGroupDAOImpl instance = null;

    private DragonGroupDAOImpl(){}

    public static DragonGroupDAOImpl getInstance(){
        if(instance == null){
            synchronized (DragonGroupDAOImpl.class){
                if(instance == null){
                    instance = new DragonGroupDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(String name, String profile, String location, double size) {
        String sql = "insert into dragongroup(name,profile,location,size) values(?,?,?,?)";
        return DBUtils.executeUpdate(sql,name,profile,location,size);
    }

    @Override
    public int delete(int dragonGroupId) {
        String sql = "delete from dragongroup where dragonGroupId = ?";
        return DBUtils.executeUpdate(sql,dragonGroupId);
    }

    @Override
    public int update(String name, String profile, String location, double size,int id) {
        String sql = "update dragongroup set name = ?,profile = ?,location = ?,size = ? where dragonGroupId = ?";
        return DBUtils.executeUpdate(sql,name,profile,location,size,id);
    }

    /**
     * 根据id找某个族群
     * */
    @Override
    public DragonGroup get(int dragonGroupId) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragongroup where dragonGroupId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,dragonGroupId);
            rs =ps.executeQuery();
            if (rs.next()) {
                return new DragonGroup(rs.getInt("dragonGroupId"),rs.getString("name"),
                        rs.getString("profile"), rs.getString("location"),rs.getFloat("size"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps, rs);
        }
        return null;
    }

    /**
     * 根据族群名字来找族群(族群名字是唯一的)
     * */
    @Override
    public DragonGroup get(String name) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragongroup where name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            rs = ps.executeQuery();
            if (rs.next()) {
               return new DragonGroup(rs.getInt("dragonGroupId"),rs.getString("name"),
                        rs.getString("profile"), rs.getString("location"),rs.getFloat("size"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps, rs);
        }
        return null;
    }

    /**
     * 找到所有的族群.
     * */
    @Override
    public List<DragonGroup> getList() {
        List<DragonGroup> dragonGroupList = new ArrayList<>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragongroup";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DragonGroup dragonGroup = new DragonGroup(rs.getInt("dragonGroupId"),rs.getString("name"),
                        rs.getString("profile"), rs.getString("location"),rs.getFloat("size"));
                dragonGroupList.add(dragonGroup);
            }
            return dragonGroupList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps, rs);
        }
        return null;
    }
}
