package model;

import entity.Foreigner;

public interface IForeignerDAO {
    void save(Foreigner foreigner);
    void save(String username,String password,String name);
    Foreigner get(String username,String password);
}
