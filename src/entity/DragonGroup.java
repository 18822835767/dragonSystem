package entity;

public class DragonGroup {
    private int dragonGroupId;
    private String name;
    private String profile;
    private String location;
    private double size;

    public DragonGroup() {
    }

    public DragonGroup(int dragonGroupId, String name, String profile, String location, double size) {
        this.dragonGroupId = dragonGroupId;
        this.name = name;
        this.profile = profile;
        this.location = location;
        this.size = size;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
