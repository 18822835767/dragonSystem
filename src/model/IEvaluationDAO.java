package model;

import entity.Evaluation;

import java.util.List;

public interface IEvaluationDAO {
    /**
     * 保存一条评价.
     */
    int save(int activityId, int foreignerId, int rank, String content, String evaluateTime);

    /**
     * 通过外邦人的Id找到评价.
     * */
    List<Evaluation> getList(int foreignerId);
}
