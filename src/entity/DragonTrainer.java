package entity;

import base.Person;

import java.util.List;

public class DragonTrainer extends Person {
    private int dragonTrainerId;
    private int dragonGroupId;

    public DragonTrainer(int dragonTrainerId,int dragonGroupId,String name, String username, String password) {
        super(name, username, password);
        this.dragonTrainerId = dragonTrainerId;
        this.dragonGroupId = dragonGroupId;
    }

    public DragonTrainer(){

    }


    public int getDragonTrainerId() {
        return dragonTrainerId;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }
}
