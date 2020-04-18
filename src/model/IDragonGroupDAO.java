package model;

import entity.DragonGroup;

import java.util.List;

public interface IDragonGroupDAO {
    /**
     * 通过具体的信息保存一条龙.
     * */
    int save(String name, String profile, String location, double size);

    /**
     * 通过id来删除一条龙.
     * */
    int delete(int id);

    /**
     * 具体的信息更新一条龙.
     * */
    int update(String name,String profile,String location,double size,int id);

    /**
     * 根据id来找到某个族群.
     * */
    DragonGroup get(int id);

    /**
     * 根据族群名字来找族群.
     * 数据库中设置族群名字为unique.
     *
     * @param name 族群名字
     * @return 族群
     * */
    DragonGroup get(String name);

    /**
     * 找到所有族群.
     * */
    List<DragonGroup> getList();
}
