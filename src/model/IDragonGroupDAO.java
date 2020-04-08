package model;

import entity.DragonGroup;

import java.util.List;

public interface IDragonGroupDAO {
    void save(DragonGroup dragonGroup);
    void save(String name, String profile, String location, double size);
    void delete(int id);
    void update(int id, DragonGroup dragonGroup);
    void update(String name,String profile,String location,double size,int id);
    DragonGroup get(int id);//根据id找某个族群

    /**
     * 根据族群名字来找族群，数据库中设置族群名字为unique.
     *
     * @param name 族群名字
     * @return 族群
     * */
    DragonGroup get(String name);//根据名字找某个族群
    List<DragonGroup> getList();//找到所有的族群
}
