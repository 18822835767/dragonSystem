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
public class ViewManage {
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

}
