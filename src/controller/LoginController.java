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
import widget.AlertTool;
import widget.DialogTool;
import widget.SingleSelectionTool;

import java.io.*;
import java.util.Optional;

public class LoginController{
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


//    public void init(){
//        File f = new File(autoLoginFile);
//        String user = null;
//        String pass = null;
//        try{
//            if (f.exists()) {
//                FileInputStream inputStream = new FileInputStream(f);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                user = reader.readLine();
//                pass = reader.readLine();
//                changeView(user,pass);
//                Stage loginStage = (Stage) username.getScene().getWindow();
//                loginStage.close();
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    /**
     * 登录按钮的点击事件.
     * */
    public void login(ActionEvent actionEvent) {
        String user = username.getText().trim();
        String pass = password.getText().trim();

        try {
            if (changeView(user, pass)) {
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
     * 如果登陆的是驯龙高手，则将族群id传入驯龙高手的控制器中，以便得到驯龙高手所在的族群id，从而得到驯龙高手所在的族群。
     * 然后再调用控制器的方法进行初始化。
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
            FXMLLoader fx = new FXMLLoader();
            Stage stage = new Stage();
            fx.setLocation(fx.getClassLoader().getResource(stageUrl));
            HBox root = (HBox) fx.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(stageTitle);
            stage.setWidth(700);
            stage.setHeight(500);
            if (dragonTrainer != null) {
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
     * 这里只为驯龙高手和外邦人提供注册。龙妈设定上只有一个，在sql语句中直接插入。
     * 通过自己封转好的单选框来选择注册对象。
     * 外邦人注册时默认的金钱为100元。
     */
    public void regist(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);
        Text text = new Text("请选择注册的对象:");
        vBox.getChildren().add(text);

        String [] buttonName = {"外邦人","驯龙高手"};
        //使用自己封转好的单选框
        RadioButton[] radioButtons = SingleSelectionTool.singSelection(vBox,buttonName,0);

        vBox.getChildren().addAll(radioButtons[0], radioButtons[1]);

        Dialog<ButtonType> dialog = DialogTool.showDialog("注册", vBox, "确定", null);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (radioButtons[0].isSelected()) {
                GridPane gridPane = new GridPane();

                Label l_name = new Label("名字:");
                Label l_username = new Label("用户名");
                Label l_password = new Label("密码");

                TextField t_name = new TextField();
                TextField t_username = new TextField();
                PasswordField p_password = new PasswordField();

                gridPane.add(l_name, 0, 0);
                gridPane.add(t_name, 1, 0);
                gridPane.add(l_username, 0, 1);
                gridPane.add(t_username, 1, 1);
                gridPane.add(l_password, 0, 2);
                gridPane.add(p_password, 1, 2);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = p_password.getText().trim();

                    new ForeignerDAOImpl().save(username, password, name);

                    AlertTool.alert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                }
            } else if (radioButtons[1].isSelected()) {
                GridPane gridPane = new GridPane();

                Label l_name = new Label("名字:");
                Label l_username = new Label("用户名:");
                Label l_password = new Label("密码:");
                Label l_dragonGroupId = new Label("族群Id:");

                TextField t_name = new TextField();
                TextField t_username = new TextField();
                PasswordField p_password = new PasswordField();
                TextField t_dragonGroupId = new TextField();

                gridPane.add(l_name, 0, 0);
                gridPane.add(t_name, 1, 0);
                gridPane.add(l_username, 0, 1);
                gridPane.add(t_username, 1, 1);
                gridPane.add(l_password, 0, 2);
                gridPane.add(p_password, 1, 2);
                gridPane.add(l_dragonGroupId, 0, 3);
                gridPane.add(t_dragonGroupId, 1, 3);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("注册信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = p_password.getText().trim();
                    int dragonGroupId = Integer.valueOf(t_dragonGroupId.getText().trim());

                    new DragonTrainerDAOImpl().save(dragonGroupId, name, username, password);

                    AlertTool.alert(Alert.AlertType.INFORMATION, null, null, "注册成功");
                }
            }
        }
    }
}
