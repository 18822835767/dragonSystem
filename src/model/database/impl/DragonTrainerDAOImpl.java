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
    public void save(DragonTrainer dT) {
        String sql = "insert into dragontrainer(dragonTrainerId,dragonGroupId,name,username,password) values(?,?,?,?)";
        executeUpdate(sql, dT.getDragonTrainerId(), dT.getDragonGroupId(), dT.getName(), dT.getUsername(), dT.getPassword());
    }

    @Override
    public void delete(int dragonTrainerId) {
        String sql = "delete from dragontrainer where dragonTrainerId = ?";
        executeUpdate(sql, dragonTrainerId);
    }

    @Override
    public void updata(int dragonTrainerId, DragonTrainer dragonTrainer) {
        String sql = "update name = ? set where dragonTrainerId = ?";
        executeUpdate(sql, dragonTrainer.getName(), dragonTrainerId);
    }

    //id查询
    @Override
    public DragonTrainer get(int dragonTrainerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from dragontrainer where dragonTrainerId = ?";
            rs = DBUtils.executeQuery(conn,ps,sql,dragonTrainerId);
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
        String sql = "select * from dragontrainer where username = ? and password = ?";
        rs = DBUtils.executeQuery(conn,ps,sql,username,password);
        try {
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
        Connection conn = null;
        PreparedStatement ps = null;
        List<DragonTrainer> dragonTrainerList = new ArrayList<>();
        ResultSet rs = null;
        String sql = "select * from dragontrainer";
        try {
            rs = DBUtils.executeQuery(conn,ps,sql);
            while (rs.next()) {
                DragonTrainer dragonTrainer = new DragonTrainer(rs.getInt("dragonTrainerId"), rs.getInt("dragonGroupId")
                        , rs.getString("name"), rs.getString("username"), rs.getString("password"));
                dragonTrainerList.add(dragonTrainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return dragonTrainerList;
    }
}
