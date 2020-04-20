package controller;

import entity.DragonMom;
import entity.DragonTrainer;
import entity.Foreigner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.IDragonMomDAO;
import model.IDragonTrainerDAO;
import model.IForeignerDAO;
import util.*;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.SingleValueTool;

import java.io.*;
import java.util.Map;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signUp;
    @FXML
    private Button sighIn;
    @FXML
    private CheckBox autoLogin;

    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    private IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();

    private IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    public static final String autoLoginFile = "autoLogin.txt";

    /**
     * 先判断用户是否保存了登录信息，保存了就读取信息，自动登录.
     */
    public void init() {
        File f = new File(autoLoginFile);
        BufferedReader reader = null;
        String user;
        String pass;
        try {
            //判断是否要自动登录
            if (f.exists()) {
                FileInputStream inputStream = new FileInputStream(f);
                reader = new BufferedReader(new InputStreamReader(inputStream));
                user = reader.readLine();
                pass = reader.readLine();
                changeView(user, Encrypt.getEncrypt(pass));//解密
                ViewManager.closeView(username);//关闭登录窗口
                System.out.println("自动登录成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 登录按钮的点击事件.
     */
    @FXML
    public void login(ActionEvent actionEvent) {
        //从输入框和密码框中得到账号密码
        String user = username.getText().trim();
        String pass = password.getText().trim();
        try {

            //调用方法判断登陆用户是谁
            if (changeView(user, pass)) {
                //如果用户勾选了自动登录，则保存信息
                if (autoLogin.isSelected()) {
                    saveLoginInfo(user, pass);
                } else {
                    //如果用户没有勾选自动登录。文件存在则删除。
                    File f = new File(autoLoginFile);
                    if (f.exists()) {
                        f.delete();
                    }
                }
                //关闭登陆界面
                ViewManager.closeView(username);
            } else {
                AlertTool.showAlert(Alert.AlertType.WARNING, null, "登陆失败", "用户名或密码输入错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从登录界面切换到各个用户的主界面.
     * 如果登陆的是驯龙高手，则将族群id传入驯龙高手的控制器中，以便得到驯龙高手所在的族群id，从而得到驯龙高手所在的族群，
     * 然后再调用驯龙高手控制器的方法进行初始化,其他类似。
     *
     * @param username 输入的用户名
     * @param password 输入的密码
     */
    public boolean changeView(String username, String password) throws IOException {
        boolean loginSuccess = false;
        String stageUrl = null;
        String stageTitle = null;
        DragonMom dragonMom;
        DragonTrainer dragonTrainer = null;
        Foreigner foreigner = null;
        if ((dragonMom = iDragonMomDAO.get(username, password)) != null) {
            stageUrl = ViewManager.MOM_URL;
            stageTitle = "龙妈您好";
            loginSuccess = true;
        } else if ((dragonTrainer = iDragonTrainerDAO.get(username, password)) != null) {
            stageUrl = ViewManager.TRAINER_URL;
            stageTitle = "驯龙高手您好";
            loginSuccess = true;
        } else if ((foreigner = iForeignerDAO.get(username, password)) != null) {
            stageUrl = ViewManager.FOREIGNER_URL;
            stageTitle = "外邦人您好";
            loginSuccess = true;
        }
        if (loginSuccess) {
            //切换到对应人物的主界面
            FXMLLoader fx = ViewManager.openView(stageUrl, stageTitle, 700.0, 500.0);

            //如果登陆的是龙妈
            if (dragonMom != null) {
                //得到龙妈的控制器，传入龙妈的实例对象
                DragonMomController dragonMomController = (DragonMomController) fx.getController();
                dragonMomController.setDragonMom(dragonMom);
                dragonMomController.init();
            }
            //如果登陆的是驯龙高手
            if (dragonTrainer != null) {
                //得到控制器，传入族群Id，调用初始化方法.
                DragonTrainerController dragonTrainerController = (DragonTrainerController) fx.getController();
                int dragonGroupId = dragonTrainer.getDragonGroupId();
                dragonTrainerController.setDragonGroupId(dragonGroupId);
                dragonTrainerController.init();
            }
            //如果登陆的是外邦人
            if (foreigner != null) {
                //得到外邦人的控制器，传入登陆的外邦人的实例对象
                ForeignerController foreignerController = (ForeignerController) fx.getController();
                foreignerController.setForeigner(foreigner);
                foreignerController.init();
            }

        }
        return loginSuccess;
    }

    /**
     * 注册.
     * 这里只为驯龙高手和外邦人提供注册。设定上龙妈只有一个，在sql语句中直接插入。
     * 通过自己封转好的单选框来选择注册对象。
     * 外邦人注册时默认的金钱为100元。
     */
    @FXML
    public void regist(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);
        Text text = new Text("请选择注册的对象:");
        vBox.getChildren().add(text);

        //使用自己封转好的单选框,选择注册对象
        String[] buttonName = {"外邦人", "驯龙高手"};
        Map<String, RadioButton> buttonMap = SingleValueTool.singleValue(buttonName, 0);

        vBox.getChildren().addAll(buttonMap.get("外邦人"), buttonMap.get("驯龙高手"));

        //封转好的自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("注册", vBox, "确定", null);
        Optional<ButtonType> result = dialog.showAndWait();

        //如果用户点击了注册的“确定”按钮
        if (result.isPresent()) {
            //“外邦人”和“驯龙高手”注册时都要输入的信息
            GridPane gridPane = new GridPane();

            Label l_name = new Label("名字:");
            Label l_username = new Label("用户名:");
            Label l_password = new Label("密码:");

            TextField t_name = new TextField();
            TextField t_username = new TextField();
            TextField t_password = new TextField();

            gridPane.add(l_name, 0, 0);
            gridPane.add(t_name, 1, 0);
            gridPane.add(l_username, 0, 1);
            gridPane.add(t_username, 1, 1);
            gridPane.add(l_password, 0, 2);
            gridPane.add(t_password, 1, 2);

            gridPane.setVgap(10);

            if (buttonMap.get("外邦人").isSelected()) {
                //弹出弹窗。除了名字，用户名，密码之外，外邦人不需要输入额外的信息。
                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    //保存用户数据，存入数据库
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = t_password.getText().trim();

                    if (CheckValid.isEmpty(name, username, password)) {
                        //判断信息是否填写完整
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "信息填写不完整");
                    } else if (!CheckValid.isValidUsername(username)) {
                        //判断用户名是否已注册
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "用户名已注册");
                    } else {
                        //成功注册
                        iForeignerDAO.save(username, password, name);
                        AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                    }

                }

            } else if (buttonMap.get("驯龙高手").isSelected()) {
                //除了名字，用户名，密码之外，外邦人需要额外输入族群Id.
                Label l_groupId = new Label("族群Id:");

                TextField t_groupId = new TextField();

                gridPane.add(l_groupId, 0, 3);
                gridPane.add(t_groupId, 1, 3);

                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = t_password.getText().trim();
                    int dragonGroupId = 0;
                    try {
                        //输入的ID是否为整数
                        dragonGroupId = Integer.parseInt(t_groupId.getText().trim());
                    } catch (Exception e) {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "非法输入");
                        return;
                    }

                    if (CheckValid.isEmpty(name, username, password, t_groupId.getText().trim()) ||
                            !CheckValid.isValidUsername(username)) {
                        //判断是否有空的信息以及用户名是否重复
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "信息填写不完整" +
                                "或者用户名已注册");
                        return;
                    }

                    int items = iDragonTrainerDAO.save(dragonGroupId, name, username, password);

                    if (items == 0) {//说明没有插入数据，错误弹窗提示
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是族群不存在");
                    } else {
                        AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                    }
                }
            }
        }
    }

    /**
     * 保存自动登录的信息.
     */
    public void saveLoginInfo(String user, String pass) {
        //保存登陆信息的文件
        File f = new File(autoLoginFile);

        //如果存在,先删除文件
        if (f.exists()) {
            f.delete();
        }

        //文件中保存登录信息
        BufferedWriter writer = null;
        try {
            f.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            writer.write(user);
            writer.newLine();
            writer.write(Encrypt.setEncrypt(pass));//加密保存
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
