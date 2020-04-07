package widget;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class TextInputDialogTool {
    private TextInputDialogTool(){

    }

    public static Optional<String> textInputDialog(String title,String headerText,String contextText){
        TextInputDialog input = new TextInputDialog();
        input.setTitle(title);
        input.setHeaderText(headerText);
        input.setContentText(contextText);
        Optional<String> result = input.showAndWait();
        return result;
    }
}
