package util;

import entity.DragonTrainer;
import entity.Foreigner;
import model.IDragonMomDAO;
import model.IDragonTrainerDAO;
import model.IForeignerDAO;

/**
 * 检测用户的输入是否合理.
 */
public class CheckValid {
    private static IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();
    private static IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();
    private static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private CheckValid() {
    }

    /**
     * 判断是否有空字符串.
     *
     * @param params 字符串，可变参数
     * @return 布尔值
     */
    public static boolean isEmpty(String... params) {
        for (String param : params) {
            if (param.equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户名是否重复(相对于所有用户而言).
     */
    public static boolean isValidUsername(String user) {
        //是否和龙妈的用户名重复
        if (iDragonMomDAO.get().getPassword().equals(user)) {
            return false;
        }

        //是否和驯龙高手用户名重复
        for (DragonTrainer trainer : iDragonTrainerDAO.getList()) {
            if (trainer.getUsername().equals(user)) {
                return false;
            }
        }

        //是否和外邦人用户名重复
        for (Foreigner foreigner : iForeignerDAO.getList()) {
            if (foreigner.getUsername().equals(user)) {
                return false;
            }
        }

        return true;
    }
}
