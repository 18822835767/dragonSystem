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
    public int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //加载驱动、获取连接
            conn = DBUtils.getConnection();
            //获取数据库预编译操作对象
            ps = conn.prepareStatement(sql);
            //params参数遍历
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, null);
        }
        return 0;
    }

    @Override
    public void save(Dragon d) {
        int training = d.isTraining() ? 1 : 0;
        int healthy = d.isHealthy() ? 1 : 0;
        String sql = "insert into dragon(dragonId,dragonGroupId,name,profile,training,healthy,sex,age) values(?,?,?,?,?,?,?,?)";
        executeUpdate(sql, d.getDragonId(), d.getDragonGroupId(), d.getName(), d.getProfile(), training, healthy, d.getSex(), d.getAge());
    }

    @Override
    public void delete(int dragonId) {
        String sql = "delete from dragon where dragonId = ?";
        executeUpdate(sql, dragonId);
    }

    @Override
    public void updata(int dragonId, Dragon d) {
        int training = d.isTraining() ? 1 : 0;
        int healthy = d.isHealthy() ? 1 : 0;
        String sql = "update dragon set name = ?,profile = ?,training = ?,healthy = ?,sex = ?,age = ? where dragonId = ?";
        executeUpdate(sql, d.getName(), d.getProfile(), training, healthy, d.getSex(), d.getAge());
    }

    @Override
    public Dragon get(int dragonId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from dragon where dragonId = ?";
        try {
            rs = DBUtils.executeQuery(conn,ps,sql, dragonId);
            if (rs.next()) {
                //boolean与int的值转换
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, (char) rs.getInt("sex"), rs.getInt("age"));
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
        Connection conn = null;
        PreparedStatement ps = null;
        List<Dragon> dragonList = new ArrayList<>();
        ResultSet rs = null;
        String sql = "select * from dragon where dragonGroupId = ?";
        try {
            rs = DBUtils.executeQuery(conn,ps,sql, dragonGroupId);
            while (rs.next()) {
                //boolean与int的值转换,因为表和类中该字段的属性不同
                boolean training = rs.getInt("training") == 1;
                boolean healthy = rs.getInt("healthy") == 1;
                Dragon dragon = new Dragon(rs.getInt("dragonId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                        rs.getString("profile"), training, healthy, (char) rs.getInt("sex"), rs.getInt("age"));
                dragonList.add(dragon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return dragonList;
    }
}

