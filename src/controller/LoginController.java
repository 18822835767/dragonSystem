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
import util.PaneFilling;
import util.DAOFactory;
import util.Encrypt;
import util.ViewManager;
import widget.AlertTool;
import widget.DialogTool;
import widget.SingleValueTool;

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

    public static final String dragonMomUrl = "view/DragonMom.fxml";
    public static final String dragonTrainerUrl = "view/DragonTrainer.fxml";
    public static final String foreignerUrl = "view/Foreigner.fxml";
    public static final String autoLoginFile = "autoLogin.txt";

    /**
     * 先判断用户是否保存了登录信息，保存了就读取信息，自动登录.
     */
    public void init() {
        File f = new File(autoLoginFile);
        BufferedReader reader = null;
        String user = null;
        String pass = null;
        try {
            //判断是否要自动登录
            if (f.exists()) {
                FileInputStream inputStream = new FileInputStream(f);
                reader = new BufferedReader(new InputStreamReader(inputStream));
                user = reader.readLine();
                pass = reader.readLine();
                changeView(user, Encrypt.getInstance().getEncrypt(pass));//解密
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
     * 然后再调用驯龙高手控制器的方法进行初始化。
     *
     * @param username 输入的用户名
     * @param password 输入的密码
     */
    public boolean changeView(String username, String password) throws IOException {
        boolean loginSuccess = false;
        String stageUrl = null;
        String stageTitle = null;
        DragonMom dragonMom ;
        DragonTrainer dragonTrainer = null;
        Foreigner foreigner = null;
        if ((dragonMom = iDragonMomDAO.get(username, password) )!= null) {
            stageUrl = dragonMomUrl;
            stageTitle = "龙妈您好";
            loginSuccess = true;
        } else if ((dragonTrainer = iDragonTrainerDAO.get(username, password)) != null) {
            stageUrl = dragonTrainerUrl;
            stageTitle = "驯龙高手您好";
            loginSuccess = true;
        } else if ((foreigner = iForeignerDAO.get(username, password)) != null) {
            stageUrl = foreignerUrl;
            stageTitle = "外邦人您好";
            loginSuccess = true;
        }
        if (loginSuccess) {
            //切换到对应人物的主界面
            FXMLLoader fx = ViewManager.openView(stageUrl,stageTitle,700.0,500.0);

            //如果登陆的是龙妈
            if(dragonMom != null){
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
                dragonTrainerController.Init();
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
    public void regist(ActionEvent actionEvent) {
//        //获取当前已有的用户名，防止重复
//        List<String> userList = new ArrayList<>();
//        List<String> trainerUsers = new DragonTrainerDAOImpl().getUserNameList();
//        List<String> foreignerUsers = new ForeignerDAOImpl().getUserNameList();
//        userList.add(new DragonMomDAOImpl().getUsername());
//        for(String user : trainerUsers){
//            userList.add(user);
//        }
//        for(String user : foreignerUsers){
//            userList.add(user);
//        }
//

        VBox vBox = new VBox(10);
        Text text = new Text("请选择注册的对象:");
        vBox.getChildren().add(text);

        //使用自己封转好的单选框,选择注册对象
        String[] buttonName = {"外邦人", "驯龙高手"};
        RadioButton[] radioButtons = SingleValueTool.singSelection(vBox, buttonName, 0);

        vBox.getChildren().addAll(radioButtons[0], radioButtons[1]);

        //封转好的自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("注册", vBox, "确定", null);
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            if (radioButtons[0].isSelected()) {
                GridPane gridPane = new GridPane();

                //调用工具类，加载布局中的数据
                String[] labelTexts = {"名字:", "用户名", "密码"};
                String[] textFieldContents = {"", "", ""};//使传入的两个数组长度相同
                Map<String,TextField> map = PaneFilling.getInstance().addForGridPane(gridPane, labelTexts, textFieldContents);

                gridPane.setVgap(10);

                //弹出弹窗
                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    //保存用户数据，存入数据库
                    String name = map.get("名字:").getText().trim();
                    String username =  map.get("用户名").getText().trim();
                    String password =  map.get("密码").getText().trim();

                    int items = iForeignerDAO.save(username, password, name);

                    if (items == 0) {//说明没有插入数据，错误弹窗提示
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是用户名已注册");
                    } else {//错误弹窗提示
                        AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                    }

                }
            } else if (radioButtons[1].isSelected()) {
                GridPane gridPane = new GridPane();

                //加载布局中的数据
                String[] labelTexts = {"名字:", "用户名:", "密码:", "族群Id:"};
                String[] textFieldContents = {"", "", "", ""};//使传入的两个数组长度相同。
                Map<String,TextField> map = PaneFilling.getInstance().addForGridPane(gridPane, labelTexts, textFieldContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = map.get("名字:").getText().trim();
                    String username = map.get("用户名:").getText().trim();
                    String password = map.get( "密码:").getText().trim();
                    int dragonGroupId = Integer.parseInt(map.get("族群Id:").getText().trim());

                    int items = iDragonTrainerDAO.save(dragonGroupId, name, username, password);

                    if (items == 0) {//说明没有插入数据，错误弹窗提示
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是族群不存在" +
                                "或者用户名已注册");
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
            writer.write(Encrypt.getInstance().setEncrypt(pass));//加密保存
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
