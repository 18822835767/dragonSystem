package controller;

import entity.DragonTrainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.database.impl.DragonMomDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import model.database.impl.ForeignerDAOImpl;
import util.AddNodeForPane;
import widget.AlertTool;
import widget.DialogTool;
import widget.SingleSelectionTool;
import java.io.*;
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

    public static final String dragonMomUrl = "view/DragonMom.fxml";
    public static final String dragonTrainerUrl = "view/DragonTrainer.fxml";
    public static final String foreignerUrl = "view/Foreigner.fxml";
    public static final String autoLoginFile = "autoLogin.txt";

    /**
     * 先判断用户是否保存了登录信息，保存了就读取信息，自动登录.
     * */
    public void init(){
        File f = new File(autoLoginFile);
        BufferedReader reader = null;
        String user = null;
        String pass = null;
        try{
            if (f.exists()) {
                FileInputStream inputStream = new FileInputStream(f);
                reader = new BufferedReader(new InputStreamReader(inputStream));
                user = reader.readLine();
                pass = reader.readLine();
                changeView(user,pass);
                Stage loginStage = (Stage) username.getScene().getWindow();
                loginStage.close();
                System.out.println("自动登录成功");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
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
                if(autoLogin.isSelected()){
                    saveLoginInfo(user,pass);
                }else{
                    //如果用户没有勾选自动登录。文件存在则删除。
                    File f = new File(autoLoginFile);
                    if(f.exists()){
                        f.delete();
                    }

                }

                //关闭登陆界面
                Stage loginStage = (Stage) username.getScene().getWindow();
                loginStage.close();
            } else {
                AlertTool.alert(Alert.AlertType.WARNING, null, "登陆失败", "用户名或密码输入错误");
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
        Boolean loginSuccess = false;
        String stageUrl = null;
        String stageTitle = null;
        DragonTrainer dragonTrainer = null;
        if (new DragonMomDAOImpl().get(username, password) != null) {
            stageUrl = dragonMomUrl;
            stageTitle = "龙妈您好";
            loginSuccess = true;
        } else if ((dragonTrainer = new DragonTrainerDAOImpl().get(username, password)) != null) {
            stageUrl = dragonTrainerUrl;
            stageTitle = "驯龙高手您好";
            loginSuccess = true;
        } else if (new ForeignerDAOImpl().get(username, password) != null) {
            stageUrl = foreignerUrl;
            stageTitle = "外邦人您好";
            loginSuccess = true;
        }
        if (loginSuccess) {
            //切换到对应人物的主界面
            FXMLLoader fx = new FXMLLoader();
            Stage stage = new Stage();
            fx.setLocation(fx.getClassLoader().getResource(stageUrl));
            HBox root = (HBox) fx.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(stageTitle);
            stage.setWidth(700);
            stage.setHeight(500);
            //如果登陆的是驯龙高手
            if (dragonTrainer != null) {
                //得到控制器，出入族群Id，调用初始化方法.
                DragonTrainerController dragonTrainerController = (DragonTrainerController) fx.getController();
                int dragonGroupId = dragonTrainer.getDragonGroupId();
                dragonTrainerController.setDragonGroupId(dragonGroupId);
                dragonTrainerController.Init();
            }
            stage.show();
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
        RadioButton[] radioButtons = SingleSelectionTool.singSelection(vBox, buttonName, 0);

        vBox.getChildren().addAll(radioButtons[0], radioButtons[1]);

        //封转好的自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("注册", vBox, "确定", null);
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            if (radioButtons[0].isSelected()) {
                GridPane gridPane = new GridPane();

                //调用工具类，加载布局中的数据
                String [] labelTexts = {"名字:","用户名","密码"};
                String [] textFieldContents = {"","",""};//使传入的两个数组长度相同
                TextField [] textFields = AddNodeForPane.addForGridPane(gridPane,labelTexts,textFieldContents);

                gridPane.setVgap(10);

                //弹出弹窗
                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    //保存用户数据，存入数据库
                    String name = textFields[0].getText().trim();
                    String username = textFields[1].getText().trim();
                    String password = textFields[2].getText().trim();

                    new ForeignerDAOImpl().save(username, password, name);

                    AlertTool.alert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                }
            } else if (radioButtons[1].isSelected()) {
                GridPane gridPane = new GridPane();

                //加载布局中的数据
                String [] labelTexts = {"名字:","用户名:","密码:","族群Id:"};
                String [] textFieldContents = {"","","",""};//使传入的两个数组长度相同。
                TextField [] textFields = AddNodeForPane.addForGridPane(gridPane,labelTexts,textFieldContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = textFields[0].getText().trim();
                    String username = textFields[1].getText().trim();
                    String password = textFields[2].getText().trim();
                    int dragonGroupId = Integer.parseInt(textFields[3].getText().trim());

                    new DragonTrainerDAOImpl().save(dragonGroupId, name, username, password);

                    AlertTool.alert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                }
            }
        }
    }

    /**
     * 保存自动登录的信息.
     * */
    public void saveLoginInfo(String user,String pass){
        //保存登陆信息的文件
        File f = new File(autoLoginFile);

        //如果存在,先删除文件
        if(f.exists()){
            f.delete();
        }

        //文件中保存登录信息
        BufferedWriter writer = null;
        try {
            f.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            writer.write(user);
            writer.newLine();
            writer.write(pass);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
