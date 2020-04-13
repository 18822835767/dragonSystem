package model;

import entity.Ticket;

public interface ITicketDAO {
    /**
     * 通过具体的信息保存一张票.
     *
     * @param foreignerId 外邦人的Id
     * */
    int save(int foreignerId, double price, String type, String buyTime, int times,boolean back);

    int delete(int foreignerId);

    /**
     *更新信物的有效入园次数.
     *
     * @param ticketId 票的Id
     * @param times 票目前的有效次数
     * @return 返回受影响的条数
     * */
    int update(int ticketId,int times);

    /**
     * 更新是否退票的状态.
     * */
    int update(int ticketId,boolean back);

    /**
     * 得到Ticket实例对象.
     *
     * @param foreignerId 外邦人Id
     * @return Ticket实例对象
     * */
    Ticket get(int foreignerId);
}
