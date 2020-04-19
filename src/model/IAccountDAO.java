package model;

import entity.Account;

import java.util.List;

public interface IAccountDAO {
    /**
     * 保存一条账目.
     * */
    int save(int foreignerId, double money, String createTime, String status);

    /**
     * 通过外邦人的Id拿到外邦人对应的账目.
     * */
    List<Account> getListById(int foreignerId);

    /**
     * 得到龙之谷内所有的账目.
     * */
    List<Account> getAllList();
}
