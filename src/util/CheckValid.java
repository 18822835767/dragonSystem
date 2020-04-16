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
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("")) {
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

        for (DragonTrainer trainer : iDragonTrainerDAO.getList()) {
            if (trainer.getUsername().equals(user)) {
                return false;
            }
        }

        for (Foreigner foreigner : iForeignerDAO.getList()) {
            if (foreigner.getUsername().equals(user)) {
                return false;
            }
        }

        return true;
    }


}
