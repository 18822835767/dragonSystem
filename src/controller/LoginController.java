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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.database.impl.DragonMomDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import model.database.impl.ForeignerDAOImpl;

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
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setHeaderText("登陆失败");
                warning.setContentText("用户名或密码输入错误");
                warning.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断是否登陆成功以及用户是哪个，从而加载不同的.fxml
    public boolean userJudge(String username, String password) throws Exception {
        FXMLLoader fx = new FXMLLoader();
        Stage stage = new Stage();
        if (new DragonMomDAOImpl().get(username, password) != null) {
            fx.setLocation(fx.getClassLoader().getResource("view/DragonMom.fxml"));
            HBox root = (HBox) fx.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("龙妈您好");
            stage.setWidth(600);
            stage.setHeight(500);
            stage.show();
            return true;
        } else if (new DragonTrainerDAOImpl().get(username, password) != null) {
            System.out.println("驯龙高手");
            return true;
        } else if (new ForeignerDAOImpl().get(username, password) != null) {
            System.out.println("外邦人");
            return true;
        }
        return false;
    }

    public void regist(ActionEvent actionEvent) {
    }


}
