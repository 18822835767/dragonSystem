package entity;

import base.Person;

import java.util.List;

public class DragonMom extends Person {
    private int dragonMomId;
    private double moneyTub;//金库作为一个属性

    public DragonMom(int dragonMomId,String name, String username, String password,double moneyTub) {
        super(name, username, password);
        this.dragonMomId = dragonMomId;
        this.moneyTub = moneyTub;
    }

    public double getMoneyTub() {
        return moneyTub;
    }
}
