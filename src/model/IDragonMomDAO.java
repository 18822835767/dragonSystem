package model;

import entity.DragonMom;

public interface IDragonMomDAO {
    DragonMom get(String username,String password);
}
