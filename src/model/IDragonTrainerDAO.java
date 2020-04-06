package model;

import entity.DragonTrainer;

import java.util.List;

public interface IDragonTrainerDAO {
    void save(DragonTrainer dragonTrainer);
    void delete(int id);
    void updata(int id,DragonTrainer dragonTrainer);
    DragonTrainer get(int id);
    DragonTrainer get(String username,String password);
    List<DragonTrainer> getList();//得到所有的驯龙高手
}
