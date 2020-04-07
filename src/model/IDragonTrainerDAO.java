package model;

import entity.DragonTrainer;

import java.util.List;

public interface IDragonTrainerDAO {
    void save(DragonTrainer dragonTrainer);
    void save(int dragonGroupId,String name,String username,String password);
    void delete(int id);
    void update(int id,DragonTrainer dragonTrainer);
    void update(int id,int dragonGroupId,String name,String username,String password);
    DragonTrainer get(int id);
    DragonTrainer get(String username,String password);
    List<DragonTrainer> getList();//得到所有的驯龙高手
}
