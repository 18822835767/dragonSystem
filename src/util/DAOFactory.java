package util;

import model.*;
import model.database.impl.*;

/**
 *工厂类，向外提供静态的方法来产生DAO实例
 * */
public class DAOFactory {
    /**
     * 静态方法.
     *
     * @return IDragonDAO的实现对象
     * */
    public static IDragonDAO getDragonDAOInstance(){
        return new DragonDAOImpl();
    }

    public static IDragonGroupDAO getDragonGroupDAOInstance(){
        return new DragonGroupDAOImpl();
    }

    public static IDragonMomDAO getDragonMomDAOInstance(){
        return new DragonMomDAOImpl();
    }

    public static IDragonTrainerDAO getDragonTrainerDAOInstance(){
        return new DragonTrainerDAOImpl();
    }

    public static IForeignerDAO getForeignerDAOInstance(){
        return new ForeignerDAOImpl();
    }

    public static ITicketDAO getTicketDAOInstance(){
        return new TicketDAOImpl();
    }
}
