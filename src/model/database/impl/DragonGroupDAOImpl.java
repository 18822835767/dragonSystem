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

public class DragonGroupDAOImpl implements IDragonGroupDAO {
    public int executeUpdate(String sql,Object... params){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //加载驱动、获取连接
            conn = DBUtils.getConnection();
            //获取数据库预编译操作对象
            ps = conn.prepareStatement(sql);
            //params参数遍历
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1,params[i]);
            }
            return ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,null);
        }
        return 0;
    }

    //目前没用
    @Override
    public void save(DragonGroup dragonGroup) {
        String sql = "insert into dragongroup(dragonGroupId,name,profile,location,size) values(?,?,?,?,?)";
        executeUpdate(sql,dragonGroup.getId(),dragonGroup.getName(),dragonGroup.getProfile(),dragonGroup.getLocation(),
                dragonGroup.getSize());
    }

    @Override
    public void save(String name, String profile, String location, double size) {
        String sql = "insert into dragongroup(name,profile,location,size) values(?,?,?,?)";
        executeUpdate(name,profile,location,size);
    }

    @Override
    public void delete(int dragonGroupId) {
        String sql = "delete from dragongroup where dragonGroupId = ?";
        executeUpdate(sql,dragonGroupId);
    }

    @Override
    public void updata(int dragonGroupId, DragonGroup dragonGroup) {
        String sql = "update dragongroup set name = ?,profile = ?,location = ?,size = ? where dragonGroupId = ?";
        executeUpdate(sql,dragonGroup.getName(),dragonGroup.getProfile(),dragonGroup.getLocation(),dragonGroup.getSize(),
                dragonGroupId);
    }

    //根据id找某个族群
    @Override
    public DragonGroup get(int dragonGroupId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragongroup where dragonGroupId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,dragonGroupId);
            rs =ps.executeQuery();
            if (rs.next()) {
                DragonGroup dragonGroup = new DragonGroup(rs.getInt("dragonGroupId"),rs.getString("name"),
                        rs.getString("profile"), rs.getString("location"),rs.getFloat("size"));
                return dragonGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    @Override
    public DragonGroup get(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from dragongroup where name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            rs = ps.executeQuery();
            if (rs.next()) {
                DragonGroup dragonGroup = new DragonGroup(rs.getInt("dragonGroupId"),rs.getString("name"),
                        rs.getString("profile"), rs.getString("location"),rs.getFloat("size"));
                return dragonGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    /**
     * 根据族群名字来找族群，数据库中设置族群名字为unique
     * */

    //找到所有的族群
    @Override
    public List<DragonGroup> getList() {
        List<DragonGroup> dragonGroupList = new ArrayList<>();
        Connection conn = null;
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
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }
}
