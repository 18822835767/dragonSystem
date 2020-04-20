package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View的管理器，负责View的打开与关闭.
 * */
public class ViewManager {
    public static final String LOGIN_URL = "view/Login.fxml";
    public static final String FOREIGNER_URL = "view/Foreigner.fxml";
    public static final String TRAINER_URL = "view/DragonTrainer.fxml";
    public static final String MOM_URL = "view/DragonMom.fxml";
    public static final String BACK_TICKETS_URL = "view/DealBackTickets.fxml";
    public static final String MY_ACTIVITY_URL = "view/MyActivity.fxml";
    public static final String MOM_ACTIVITY_URL = "view/MomActivity.fxml";
    public static final String MY_EVALUATION_URL = "view/MyEvaluation.fxml";
    public static final String MOM_EVALUATION_URL = "view/MomEvaluation.fxml";
    public static final String MY_ACCOUNT_URL = "view/MyAccount.fxml";
    public static final String MOM_ACCOUNT_URL = "view/MomAccount.fxml";

    private ViewManager(){

    }

    /**
     * 负责打开View.
     * */
    public static FXMLLoader openView(String stageUrl,String stageTitle,Double stageWidth,Double stageHeight) throws IOException {
        FXMLLoader fx = new FXMLLoader();
        if(stageUrl != null){
            fx.setLocation(fx.getClassLoader().getResource(stageUrl));
        }
        Pane root = (Pane) fx.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        if(stageWidth != null){
            stage.setWidth(stageWidth);
        }
        if(stageHeight != null){
            stage.setHeight(stageHeight);
        }
        stage.show();

        return fx;
    }

    /**
     * 负责View的关闭.
     *
     * @param node 传入一个node，该node在View界面里.
     * */
    public static void closeView(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
