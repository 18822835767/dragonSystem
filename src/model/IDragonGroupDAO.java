package model;

import entity.DragonGroup;

import java.util.List;

public interface IDragonGroupDAO {
    void save(DragonGroup dragonGroup);
    void delete(int id);
    void updata(int id,DragonGroup dragonGroup);
    DragonGroup get(int id);//根据id找某个族群
    List<DragonGroup> getList();//找到所有的族群
}
