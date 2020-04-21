package entity;

import base.Person;

public class Foreigner extends Person {
    private int foreignerId;
    private double money;

    public Foreigner(){}

    public Foreigner(int foreignerId, String username, String password,String name,double money) {
        super(name, username, password);
        this.foreignerId = foreignerId;
        this.money = money;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
