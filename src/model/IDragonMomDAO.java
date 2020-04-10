package model;

import entity.DragonMom;

public interface IDragonMomDAO {
    /**
     * 账户+密码得到龙妈.
     * */
    DragonMom get(String username,String password);

    /**
     * 得到龙妈.
     * */
    String getUsername();
}
