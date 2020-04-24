package model.impl;

import entity.Evaluation;
import model.IEvaluationDAO;
import util.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDAOImpl implements IEvaluationDAO {
    private volatile static EvaluationDAOImpl instance = null;

    private EvaluationDAOImpl(){}

    public static EvaluationDAOImpl getInstance(){
        if(instance == null){
            synchronized (EvaluationDAOImpl.class){
                if(instance == null){
                    instance = new EvaluationDAOImpl();
                }
            }
        }
        return instance;
    }


    @Override
    public int save(int activityId, int foreignerId, int rank, String content, String evaluateTime) {
        String sql = "insert into evaluation(activityId,foreignerId,rank,content,evaluateTime) values(?,?,?,?,?)";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql, activityId,foreignerId,rank,content,evaluateTime);
    }

    @Override
    public int delete(int foreignerId, int evaluationId) {
        String sql = "delete from evaluation where foreignerId = ? and evaluationId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,foreignerId,evaluationId);
    }

    @Override
    public int delete(int evaluationId) {
        String sql = "delete from evaluation where evaluationId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,evaluationId);
    }

    @Override
    public int update(int evaluationId, int rank, String content, String evaluateTime) {
        String sql = "update evaluation set rank = ?,content = ?,evaluateTime = ? where evaluationId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,rank,content,evaluateTime,evaluationId);
    }

    @Override
    public Evaluation getByEvaluationId(int foreignerId, int evaluationId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from evaluation where foreignerId = ? and evaluationId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,foreignerId);
            ps.setInt(2,evaluationId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Evaluation(rs.getInt("evaluationId"),rs.getInt("activityId"),rs.getInt("foreignerId"),
                        rs.getInt("rank"),rs.getString("content"),rs.getString("evaluateTime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    @Override
    public Evaluation getByActivityId(int foreignerId, int activityId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from evaluation where foreignerId = ? and activityId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,foreignerId);
            ps.setInt(2, activityId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Evaluation(rs.getInt("evaluationId"),rs.getInt("activityId"),rs.getInt("foreignerId"),
                        rs.getInt("rank"),rs.getString("content"),rs.getString("evaluateTime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    @Override
    public List<Evaluation> getList(int foreignerId) {
        List<Evaluation> evaluationList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from evaluation where foreignerId = ?";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,foreignerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Evaluation evaluation = new Evaluation(rs.getInt("evaluationId"),rs.getInt("activityId"),
                        rs.getInt("foreignerId"),rs.getInt("rank"),rs.getString("content"),rs.getString("evaluateTime"));
                evaluationList.add(evaluation);
            }
            return evaluationList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    @Override
    public List<Evaluation> getList() {
        List<Evaluation> evaluationList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from evaluation";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Evaluation evaluation = new Evaluation(rs.getInt("evaluationId"),rs.getInt("activityId"),
                        rs.getInt("foreignerId"),rs.getInt("rank"),rs.getString("content"),rs.getString("evaluateTime"));
                evaluationList.add(evaluation);
            }
            return evaluationList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }
}
