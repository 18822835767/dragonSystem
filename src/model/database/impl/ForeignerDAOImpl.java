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
    @Override
    public void save(Foreigner f) {
        String sql = "insert into foreigner(foreignerId,username,password,name,money) values(?,?,?,?,?)";
        DBUtils.executeUpdate(sql,f.getForeignerId(),f.getUsername(),f.getPassword(),f.getName(),f.getMoney());
    }

    //用户名和密码查询
    @Override
    public Foreigner get(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from foreigner where username = ? and password = ?";
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
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
