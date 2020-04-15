package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View的管理器，负责View的打开与关闭.
 * */
public class ViewManager {
    public static String loginUrl = "view/login.fxml";
    public static String foreignerUrl = "view/Foreigner.fxml";
    public static String trainerUrl = "view/DragonTrainer.fxml";
    public static String momUrl = "view/DragonMom.fxml";
    public static String backTicketsUrl = "view/DealBackTickets.fxml";

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
