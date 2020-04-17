package model.database.impl;

import model.IEvaluationDAO;
import util.DBUtils;

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
}
