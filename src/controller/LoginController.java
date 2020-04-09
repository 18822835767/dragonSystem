package controller;

import entity.DragonTrainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.database.impl.DragonMomDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import model.database.impl.ForeignerDAOImpl;
import widget.AlertTool;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signUp;
    @FXML
    private Button sighIn;

    public void login(ActionEvent actionEvent) {
        String user = username.getText().trim();
        String pass = password.getText().trim();
        try {
            if (changeView(user,pass)) {
                Stage loginStage = (Stage) username.getScene().getWindow();
                loginStage.close();
            } else {
                AlertTool.alert(Alert.AlertType.WARNING,null,"登陆失败","用户名或密码输入错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regist(ActionEvent actionEvent) {
    }


    /**
     * 从登录界面切换到各个用户的主界面.
     * 如果登陆的是驯龙高手，则将族群id传入驯龙高手的控制器中，以便得到驯龙高手所在的族群id，从而得到驯龙高手所在的族群。
     * 然后再调用控制器的方法进行初始化。
     *
     * @param username 输入的用户名
     * @param password 输入的密码
     * */
    public boolean changeView(String username, String password) throws IOException {
        Boolean loginSuccess = false;
        String stageUrl = null;
        String stageTitle = null;
        DragonTrainer dragonTrainer = null;
        if (new DragonMomDAOImpl().get(username, password) != null) {
            stageUrl = "view/DragonMom.fxml";
            stageTitle = "龙妈您好";
            loginSuccess = true;
        } else if ( (dragonTrainer = new DragonTrainerDAOImpl().get(username, password)) != null) {
            stageUrl = "view/DragonTrainer.fxml";
            stageTitle = "驯龙高手您好";
            loginSuccess = true;
        } else if (new ForeignerDAOImpl().get(username, password) != null) {
            stageUrl = "view/Foreigner.fxml";
            stageTitle = "外邦人您好";
            loginSuccess = true;
        }
        if(loginSuccess){
            FXMLLoader fx = new FXMLLoader();
            Stage stage = new Stage();
            fx.setLocation(fx.getClassLoader().getResource(stageUrl));
            HBox root = (HBox) fx.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(stageTitle);
            stage.setWidth(700);
            stage.setHeight(500);
            if(dragonTrainer != null){
                DragonTrainerController dragonTrainerController = (DragonTrainerController)fx.getController();
                int dragonGroupId = dragonTrainer.getDragonGroupId();
                dragonTrainerController.setDragonGroupId(dragonGroupId);
                dragonTrainerController.Init();
            }
            stage.show();
        }
        return loginSuccess;
    }
}
