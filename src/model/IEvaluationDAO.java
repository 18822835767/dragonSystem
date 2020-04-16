package model;

public interface IEvaluationDAO {
    /**
     * 保存一条评价.
     * */
    int save(int dragonGroupId, int foreignerId, int rank, String content, String evaluateTime);
}
