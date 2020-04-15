package util;

import entity.DragonTrainer;
import entity.Foreigner;
import model.IDragonMomDAO;
import model.IDragonTrainerDAO;
import model.IForeignerDAO;

/**
 * 检测用户的输入是否合理.
 * 这里用单例合适吗?
 */
public class CheckValid {
    private volatile static CheckValid instance = null;
    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();
    private IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();
    private IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private CheckValid() {
    }

    public static CheckValid getInstance() {
        if (instance == null) {
            synchronized (CheckValid.class) {
                if (instance == null) {
                    instance = new CheckValid();
                }
            }
        }
        return instance;
    }

    /**
     * 检测用户的输入是否为正整数.
     *
     * @param str 字符串
     * @return 布尔值
     */
    public boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有空字符串.
     *
     * @param params 字符串，可变参数
     * @return 布尔值
     */
    public boolean isEmpty(String... params) {
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
    public boolean isValidUsername(String user) {
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
