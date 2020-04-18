package util.control;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装了一个单选框.
 */
public class SingleValueTool {
    private SingleValueTool() {

    }

    /**
     * 单选的方法，static属性.
     *
     * @param buttonName       单选按钮的名字。
     * @param defalutSelection 默认选中哪个单选按钮(下标)，不默认选中，则传入null。
     * @return 返回Map<String,RadioButton>。
     */
    public static Map<String,RadioButton> singleValue(String[] buttonName, Integer defalutSelection) {
        int buttonNum = buttonName.length;//按钮的数量

        Map<String,RadioButton> map = new HashMap<>();

        RadioButton[] radioButtons = new RadioButton[buttonNum];//按钮数组
        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < buttonNum; i++) {
            radioButtons[i] = new RadioButton(buttonName[i]);//按钮创建对象
            radioButtons[i].setToggleGroup(toggleGroup);//单选按钮设置在一组里
            map.put(buttonName[i],radioButtons[i]);
        }
        if (defalutSelection != null) {
            toggleGroup.selectToggle(radioButtons[defalutSelection]);//默认先选中哪个
        }
        return map;
    }
}
