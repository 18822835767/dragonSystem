package entity;

import java.util.List;

public class DragonGroup {
    private int dragonGroupId;
    private String name;
    private String profile;
    private String location;
    private double size;
    private int dragonNumber;
    private List<Dragon> dragonList;//怎么初始化？？

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

    public List<Dragon> getDragonList(){
        return dragonList;
    }

    public void addDragon(Dragon dragon){
        dragonList.add(dragon);
    }

    public void removeDragon(Dragon dragon){
        dragonList.remove(dragon);
    }

    public Dragon getDragon(String dragonName){
        for(Dragon dragon : dragonList){
            if(dragonName.equals(dragon.getName())){
                return dragon;
            }
        }
        return null;
    }

    public void updateDragon(Dragon oldDragon,Dragon newDragon){//我也不知道有没有用？？
        //这样设计好吗？？那么龙的信息岂不是要重新初始化？
        dragonList.remove(oldDragon);
        dragonList.add(newDragon);
    }

    public int getId() {
        return dragonGroupId;
    }

    public int getDragonNumber() {
        return dragonNumber;
    }
}
