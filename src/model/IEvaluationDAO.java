package model;

import entity.Evaluation;

import java.util.List;

public interface IEvaluationDAO {
    /**
     * 保存一条评价.
     */
    int save(int activityId, int foreignerId, int rank, String content, String evaluateTime);

    /**
     * 删除一条评价.
     *
     * @param foreignerId 外邦人Id
     * @param evaluationId "评价"Id
     * */
    int delete(int foreignerId,int evaluationId);

    /**
     * 通过外邦人的Id找到评价.
     * */
    List<Evaluation> getList(int foreignerId);

    /**
     * 得到所有的"评价"列表.
     * */
    List<Evaluation> getList();
}
