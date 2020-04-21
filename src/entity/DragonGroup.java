package entity;

public class DragonGroup {
    private int dragonGroupId;
    private String name;
    private String profile;
    private String location;
    private double size;

    public DragonGroup(){}

    public DragonGroup(int dragonGroupId,String name, String profile, String location, double size) {
        this.dragonGroupId = dragonGroupId;
        this.name = name;
        this.profile = profile;
        this.location = location;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public String getLocation() {
        return location;
    }

    public double getSize() {
        return size;
    }

    public int getId() {
        return dragonGroupId;
    }

}
