package model;

import entity.Foreigner;

import java.util.List;

public interface IForeignerDAO {
    /**
     * 通过具体信息来保存外邦人.
     * */
    int save(String username,String password,String name);

    /**
     * 更新外邦人余额.
     * */
    int update(int foreignerId,double money);

    /**
     * 账户名+密码来得到外邦人.
     * */
    Foreigner get(String username,String password);

//    /**
//     * 得到所有的外邦人.
//     * */
//    List<String> getUserNameList();
}
