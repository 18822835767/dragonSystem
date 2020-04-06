package model.database.impl;

import entity.DragonMom;
import entity.DragonTrainer;
import entity.Foreigner;
import model.IForeignerDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForeignerDAOImpl implements IForeignerDAO {
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
    public void save(Foreigner f) {
        String sql = "insert into foreigner(foreignerId,username,password,name,money) values(?,?,?,?,?)";
        executeUpdate(sql,f.getForeignerId(),f.getUsername(),f.getPassword(),f.getName(),f.getMoney());
    }

    //用户名和密码查询
    @Override
    public Foreigner get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from foreigner where username = ? and password = ?";
        rs = DBUtils.executeQuery(conn,ps,sql,username,password);
        try {
            if(rs.next()){
                Foreigner foreigner = new Foreigner(rs.getInt("foreignerId"), rs.getString("username"),
                        rs.getString("password"),rs.getString("name"),rs.getInt("money"));
                return foreigner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn,ps,rs);
        }
        return null;
    }
}
