package model;

import entity.Dragon;

import java.util.List;

public interface IDragonDAO {
    void save(Dragon dragon);
    void save(int dragonGroupId, String name, String profile, boolean training, boolean healthy, String sex, int age);
    void delete(int id);
    void updata(int id,Dragon dragon);
    Dragon get(int dragonId,String name);
    Dragon get(int id);//根据id找某条long
    List<Dragon> getList(int dragonGroupId);//找到某个族群的龙
}
