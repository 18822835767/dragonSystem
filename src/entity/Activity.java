package entity;

/**
 * 活动的实体类.
 * */
public class Activity {
    private int activityId;//活动的Id，可能没用
    private int dragonGroupId;//可能没用
    private String name;
    private String content;
    private String startTime;
    private String overTime;
    private boolean checked = false;//后面用于标记是否被checkBox选中

    public Activity(int activityId, int dragonGroupId, String name, String content, String startTime, String overTime) {
        this.name = name;
        this.content = content;
        this.startTime = startTime;
        this.overTime = overTime;
        this.dragonGroupId = dragonGroupId;
        this.activityId = activityId;
    }

    public Activity() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }

    public int getActivityId() {
        return activityId;
    }

}
