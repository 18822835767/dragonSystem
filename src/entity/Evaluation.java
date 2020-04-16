package entity;

/**
 * 评价的实体类.
 * */
public class Evaluation {
    private int evaluationId;//评价的Id，可能没用
    private int dragonGroupId;//可能没用
    private int foreignerId;//可能没用
    private int rank;
    private String content;
    private String evaluateTime;//评价的时间

    public Evaluation(int evaluationId, int dragonGroupId, int foreignerId, int rank, String content, String evaluateTime) {
        this.evaluationId = evaluationId;
        this.dragonGroupId = dragonGroupId;
        this.foreignerId = foreignerId;
        this.rank = rank;
        this.content = content;
        this.evaluateTime = evaluateTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public void setForeignerId(int foreignerId) {
        this.foreignerId = foreignerId;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }
}
