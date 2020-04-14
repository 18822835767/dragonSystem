package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.ViewManage;

import java.io.IOException;

/**
 * 添加了切换账户的功能.
 * 因为多个控制器都要用到，所以抽离出来成为类中的一个静态方法。
 * */
public class SwitchAccount {
    private SwitchAccount(){

    }

    /**
     * 切换账户的方法.
     *
     * @param button "切换账户"按钮的引用
     * */
    public static void changeUser(Button button){
        try {
            ViewManage.openView("view/login.fxml",null,420.0,280.0);

            //关闭之前的窗口
            Stage oldStage = (Stage)button.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
