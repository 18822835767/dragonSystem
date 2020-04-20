package model;

import entity.Account;

import java.util.List;

public interface IAccountDAO {
    /**
     * 保存一条账目.
     * */
    int save(int foreignerId, double money, String createTime, String status);

    /**
     * 拿到某个外邦人的"购买"或"退款"的账目.
     * */
    List<Account> getForeignerListByStatus(int foreignerId, String status);

    /**
     * 通过外邦人的Id拿到外邦人对应的账目.
     * */
    List<Account> getListById(int foreignerId);

    /**
     * 得到龙之谷内所有的"购买"或者"退款"的账目。
     * */
    List<Account> getAllListByStatus(String status);

    /**
     * 得到龙之谷内所有的账目.
     * */
    List<Account> getAllList();
}
