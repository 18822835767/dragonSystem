package entity;

public class Dragon {
    private int dragonId;
    private int dragonGroupId;
    private String name;
    private String profile;
    private boolean training = false;
    private boolean healthy = true;
    private String sex;
    private int age;

    public Dragon(int dragonId, int dragonGroupId, String name, String profile, boolean training, boolean healthy, String sex, int age) {
        this.dragonId = dragonId;
        this.dragonGroupId = dragonGroupId;
        this.name = name;
        this.profile = profile;
        this.training = training;
        this.healthy = healthy;
        this.sex = sex;
        this.age = age;
    }

    public Dragon(){

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

    public boolean isHealthy() {
        return healthy;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getDragonId() {
        return dragonId;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }
}
