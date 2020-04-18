package model;

import entity.DragonMom;

public interface IDragonMomDAO {
    /**
     * 更新金库中的钱.
     * */
    int update(double money);

    /**
     * 直接查找得到龙妈(龙妈设计上只有一个).
     * */
    DragonMom get();

    /**
     * 账户+密码得到龙妈.
     * */
    DragonMom get(String username,String password);
}
