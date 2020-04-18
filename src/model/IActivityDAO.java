package model;

import entity.Activity;

import java.time.LocalDate;
import java.util.List;

public interface IActivityDAO {
    /**
     * 保存活动信息.
     * */
    int save(int dragonGroupId,String name, String content, String startTime, String overTime);

    /**
     * 通过活动的Id来得到活动.
     * */
    Activity getById(int id);

    /**
     * 得到有效期内的活动.
     * */
    List<Activity> getValidList(LocalDate time);

    /**
     * 得到所有的活动.
     * */
    List<Activity> getList();
}
