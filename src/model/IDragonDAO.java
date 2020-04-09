package model;

import entity.Dragon;

import java.util.List;

public interface IDragonDAO {
    /**
     * 通过龙的引用，来储存一条龙.
     * */
    void save(Dragon dragon);

    /**
     * 通过具体的信息，来储存一条龙.
     * */
    void save(int dragonGroupId, String name, String profile, boolean training, boolean healthy, String sex, int age);

    /**
     * 通过龙的id(主键)来删除一条龙.
     * */
    void delete(int id);

    /**
     * id+引用来更新一条龙.
     * */
    void updata(int id,Dragon dragon);

    /**
     * 通过具体的信息，来更新一条龙.
     * */
    void update(int id,int dragonGroupId, String name, String profile, boolean training, boolean healthy,int age);

    /**
     * 获取一条龙.
     *
     * @param dragonGroupId 族群id
     * @param name 龙的名字
     * */
    Dragon get(int dragonGroupId,String name);

    /**
     * 根据龙的id来找某条龙.
     *
     * @param id 龙的id
     * */
    Dragon get(int id);

    /**
     * 找到某个族群的龙.
     *
     * @param dragonGroupId 族群id
     * */
    List<Dragon> getList(int dragonGroupId);

    /**
     * 找到所有族群的龙.
     *
     * @return 龙的List
     * */
    public List<Dragon> getList();
}
