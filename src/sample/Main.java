package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(fx.getClassLoader().getResource("view/login.fxml"));
        Pane root = (Pane) fx.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setHeight(280);
        primaryStage.setWidth(420);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
