package entity;

import base.Person;

import java.util.List;

public class DragonTrainer extends Person {
    private int dragonTrainerId;
    private int dragonGroupId;
    private DragonGroup group;
    private List<Dragon> dragonList;//需要吗？？

    public DragonTrainer(int dragonTrainerId,int dragonGroupId,String name, String username, String password) {
        super(name, username, password);
        this.dragonTrainerId = dragonTrainerId;
        this.dragonGroupId = dragonGroupId;
    }

    public DragonTrainer(){

    }

    public List<Dragon> getDragonList() {
        return dragonList;
    }

    public DragonGroup getGroup(){
        return group;
    }

    public void setGroup(DragonGroup group) {//设置族群或者修改族群
        this.group = group;
    }

    public void setDragonList(List<Dragon> dragonList) {
        this.dragonList = dragonList;
    }

    public void addDragon(Dragon dragon){
        group.addDragon(dragon);
    }

    public void removeDragon(Dragon dragon){
        group.removeDragon(dragon);
    }

    public Dragon getDragon(String dragonName){
        return group.getDragon(dragonName);
    }

    public void updateDragon(Dragon oldDragon,Dragon newDragon){//我也不知道有没有用？？
        group.updateDragon(oldDragon,newDragon);
    }

    public int getDragonTrainerId() {
        return dragonTrainerId;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }
}
