package controller;

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
            if (userJudge(user, pass)) {
                Stage loginStage = (Stage) username.getScene().getWindow();
                loginStage.close();
            } else {
                AlertTool.alert(Alert.AlertType.WARNING,null,"登陆失败","用户名或密码输入错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断是否登陆成功以及用户是哪个，从而加载不同的.fxml
    public boolean userJudge(String username, String password) throws Exception {
        if (new DragonMomDAOImpl().get(username, password) != null) {
            changeView("view/DragonMom.fxml","龙妈您好");
            return true;
        } else if (new DragonTrainerDAOImpl().get(username, password) != null) {
            changeView("view/DragonTrainer.fxml","驯龙高手您好");
            return true;
        } else if (new ForeignerDAOImpl().get(username, password) != null) {
            System.out.println("外邦人");
            return true;
        }
        return false;
    }

    public void regist(ActionEvent actionEvent) {
    }


    /**
     * 从登录界面切换到各个用户的主界面.
     *
     * @param name 要切换的界面的.fxml文件
     * @param stageTitle 主界面的stageTitle
     * */
    public void changeView(String name,String stageTitle) throws IOException {
        FXMLLoader fx = new FXMLLoader();
        Stage stage = new Stage();
        fx.setLocation(fx.getClassLoader().getResource(name));
        HBox root = (HBox) fx.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
    }
}
