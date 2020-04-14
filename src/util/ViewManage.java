package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManage {
    public static FXMLLoader openView(String stageUrl,String stageTitle,Double stageWidth,Double stageHeight) throws IOException {
        FXMLLoader fx = new FXMLLoader();
        Stage stage = new Stage();
        if(stageUrl != null){
            fx.setLocation(fx.getClassLoader().getResource(stageUrl));
        }
        HBox root = (HBox) fx.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.show();

        return fx;
    }

}
