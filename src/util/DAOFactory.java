package util;

import model.*;
import model.impl.*;

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
        return DragonDAOImpl.getInstance();
    }

    public static IDragonGroupDAO getDragonGroupDAOInstance(){
        return DragonGroupDAOImpl.getInstance();
    }

    public static IDragonMomDAO getDragonMomDAOInstance(){
        return DragonMomDAOImpl.getInstance();
    }

    public static IDragonTrainerDAO getDragonTrainerDAOInstance(){
        return DragonTrainerDAOImpl.getInstance();
    }

    public static IForeignerDAO getForeignerDAOInstance(){
        return ForeignerDAOImpl.getInstance();
    }

    public static ITicketDAO getTicketDAOInstance(){
        return TicketDAOImpl.getInstance();
    }

    public static IActivityDAO getActivityDAOInstance(){
        return ActivityDAOImpl.getInstance();
    }

    public static IEvaluationDAO getEvaluationDAOInstance(){return EvaluationDAOImpl.getInstance();}

    public static IAccountDAO getAccountDAOInstance(){
        return AccountDAOImpl.getInstance();
    }
}
