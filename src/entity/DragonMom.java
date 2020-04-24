package entity;

import base.Person;

import java.util.List;

public class DragonMom extends Person {
    private int dragonMomId;
    private double moneyTub;//金库作为一个属性

    public DragonMom() {
    }

    public DragonMom(int dragonMomId, String name, String username, String password, double moneyTub) {
        super(name, username, password);
        this.dragonMomId = dragonMomId;
        this.moneyTub = moneyTub;
    }

    public int getDragonMomId() {
        return dragonMomId;
    }

    public void setDragonMomId(int dragonMomId) {
        this.dragonMomId = dragonMomId;
    }

    public double getMoneyTub() {
        return moneyTub;
    }

    public void setMoneyTub(double moneyTub) {
        this.moneyTub = moneyTub;
    }
}
