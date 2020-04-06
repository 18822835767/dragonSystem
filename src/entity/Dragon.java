package entity;

public class Dragon {
    private int dragonId;
    private int dragonGroupId;
    private String name;
    private String profile;
    private boolean training = false;
    private boolean healthy = true;
    private char sex;
    private int age;

    public Dragon(int dragonId,int dragonGroupId,String name,String profile,boolean training,boolean healthy,char sex, int age) {
        this.dragonId = dragonId;
        this.dragonGroupId = dragonGroupId;
        this.name = name;
        this.profile = profile;
        this.training = training;
        this.healthy = healthy;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isTraining() {
        return training;
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDragonId() {
        return dragonId;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }
}
