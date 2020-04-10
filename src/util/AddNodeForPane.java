package util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * 为pane添加node.
 * 由于增删查改有大量的语句本质上一样，都是为pane添加某种node，所以抽离出来作为一个工具类使用，提供不同的方法,从而使增删查改
 * 的方法更加简洁。
 * */
public class AddNodeForPane {
    private AddNodeForPane(){

    }

    /**
     * 为pane添加TextFields.
     *
     * @param pane 要添加的pane
     * @param promptTexts 所添加的TextFields的提示性文字
     * @return 返回TextField[]
     * */
    public static TextField[] addTextFieldForPane(Pane pane, String [] promptTexts){
        int length = promptTexts.length;

        TextField[] textFields = new TextField[length];

        for(int i=0;i<length;i++){
            textFields[i] = new TextField();
            if(promptTexts[i] != null){
                textFields[i].setPromptText(promptTexts[i]);
            }
            pane.getChildren().add(textFields[i]);
        }
        return textFields;
    }

    /**
     * 为pane添加Text.
     *
     * @param pane 要添加的pane
     * @param textContents Text中的文字
     * @return 返回Text[]
     * */
    public static Text[] addTextForPane(Pane pane,String [] textContents){
        int length = textContents.length;

        Text[] texts = new Text[length];

        for(int i=0;i<length;i++){
            texts[i] = new Text();
            if(textContents[i] != null){
                texts[i].setText(textContents[i]);
            }
            pane.getChildren().add(texts[i]);
        }
        return texts;
    }

    /**
     * 为GridPane添加Label和TextField.
     * 注意2个数组长度相同。
     *
     * @param gridPane 要添加的GridPane
     * @param labelTexts Label的文字
     * @param textFiledContents TextField中的内容
     * @return 返回TextField[]
     * */
    public static TextField[] addForGridPane(GridPane gridPane,String[] labelTexts,String[] textFiledContents ){
        int length = labelTexts.length;
        Label [] labels = new Label[length];
        TextField [] textFields = new TextField[length];

        for(int i=0;i<length;i++){
            labels[i] = new Label();
            textFields[i] = new TextField();
            if(labels[i] != null){
                labels[i].setText(labelTexts[i]);
                gridPane.add(labels[i],0,i);
            }
            if(textFiledContents[i] != null){
                textFields[i].setText(textFiledContents[i]);
                gridPane.add(textFields[i],1,i);
            }
        }
        return textFields;
    }
}
