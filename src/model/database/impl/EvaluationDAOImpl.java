package model.database.impl;

import entity.DragonTrainer;
import entity.Evaluation;
import model.IEvaluationDAO;
import util.DBUtils;
import util.Encrypt;

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
        return DBUtils.executeUpdate(sql, activityId,foreignerId,rank,content,evaluateTime);
    }

    @Override
    public List<Evaluation> getList(int foreignerId) {
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
            DBUtils.close(null,ps, rs);
        }
        return null;
    }
}
