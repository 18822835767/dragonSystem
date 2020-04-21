package entity;

/**
 * 评价的实体类.
 * */
public class Evaluation {
    private int evaluationId;
    private int activityId;
    private int foreignerId;
    private int rank;
    private String content;
    private String evaluateTime;//评价的时间

    public Evaluation() {
    }

    public Evaluation(int evaluationId, int dragonGroupId, int foreignerId, int rank, String content, String evaluateTime) {
        this.evaluationId = evaluationId;
        this.activityId = dragonGroupId;
        this.foreignerId = foreignerId;
        this.rank = rank;
        this.content = content;
        this.evaluateTime = evaluateTime;
    }

    public int getRank() {
        return rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getActivityId() {
        return activityId;
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

    public String getEvaluateTime() {
        return evaluateTime;
    }
}
