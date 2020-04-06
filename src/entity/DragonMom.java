package entity;

import base.Person;

import java.util.List;

public class DragonMom extends Person {
    private int dragonMomId;
    private List<DragonTrainer> dragonTrainerList;
    private List<DragonGroup> groupList;

    public DragonMom(int dragonMomId,String name, String username, String password) {
        super(name, username, password);
        this.dragonMomId = dragonMomId;
    }

    public DragonMom(){

    }

    public List<DragonTrainer> getDragonTrainerList() {
        return dragonTrainerList;
    }

    public List<DragonGroup> getGroupList() {
        return groupList;
    }

    public void addDragonTrainer(DragonTrainer dragonTrainer) {
        dragonTrainerList.add(dragonTrainer);
    }

    public void removeDragonTrainer(DragonTrainer dragonTrainer) {
        dragonTrainerList.remove(dragonTrainer);
    }

    public DragonTrainer getDragonTrainer(String dragonTrainerName) {
        for (DragonTrainer dragonTrainer : dragonTrainerList) {
            if (dragonTrainerName.equals(dragonTrainer.getName())) {
                return dragonTrainer;
            }
        }
        return null;
    }

    public void updateDragonTrainer(DragonTrainer oldDragonTrainer, DragonTrainer newDragonTrainer) {//我也不知道有没有用？？
        //这样设计好吗？？那么驯龙高手的信息岂不是要重新初始化？
        dragonTrainerList.remove(oldDragonTrainer);
        dragonTrainerList.add(newDragonTrainer);
    }

    public int getDragonMomId() {
        return dragonMomId;
    }
}
