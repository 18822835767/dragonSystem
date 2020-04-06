package model;

import entity.Foreigner;

public interface IForeignerDAO {
    void save(Foreigner foreigner);
    Foreigner get(String username,String password);
}
