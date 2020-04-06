package entity;

import base.Person;

public class Foreigner extends Person {
    private int foreignerId;
    private int money;

    public Foreigner(int foreignerId, String username, String password,String name,int money) {
        super(name, username, password);
        this.foreignerId = foreignerId;
        this.money = money;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public int getMoney() {
        return money;
    }
}
