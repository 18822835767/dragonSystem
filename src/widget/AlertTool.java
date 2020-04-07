package widget;

import javafx.scene.control.Alert;

public class AlertTool {
    private AlertTool() {
    }

    public static void alert(Alert.AlertType type,String title,String header,String context){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
