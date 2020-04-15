package util.control;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;
/**
 * TextInputDialog，可重用的自定义控件.
 * 适合于只输入一个ID...的显示
 * */
public class TextInputDialogTool {
    private TextInputDialogTool(){

    }

    public static Optional<String> textInputDialog(String title,String headerText,String contextText){
        TextInputDialog input = new TextInputDialog();
        input.setTitle(title);
        input.setHeaderText(headerText);
        input.setContentText(contextText);
        return input.showAndWait();
    }
}
