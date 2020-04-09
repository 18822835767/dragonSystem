package entity;

import base.Person;

import java.util.List;

public class DragonMom extends Person {
    private int dragonMomId;

    public DragonMom(int dragonMomId,String name, String username, String password) {
        super(name, username, password);
        this.dragonMomId = dragonMomId;
    }

    public DragonMom(){

    }

    public int getDragonMomId() {
        return dragonMomId;
    }
}
