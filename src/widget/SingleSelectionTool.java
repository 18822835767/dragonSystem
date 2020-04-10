package widget;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 封装了一个单选框.
 */
public class SingleSelectionTool {
    private SingleSelectionTool() {

    }

    /**
     * 单选的方法，static属性.
     *
     * @param pane             传入的单选框布局方式。
     * @param buttonName       单选按钮的名字。
     * @param defalutSelection 默认选中哪个单选按钮(下标)，不默认选中，则传入null。
     * @return 返回单选按钮的对象数组。
     */
    public static RadioButton[] singSelection(Pane pane, String[] buttonName, Integer defalutSelection) {
        int buttonNum = buttonName.length;//按钮的数量

        RadioButton[] radioButtons = new RadioButton[buttonNum];//按钮数组
        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < buttonNum; i++) {
            radioButtons[i] = new RadioButton(buttonName[i]);//按钮创建对象
            radioButtons[i].setToggleGroup(toggleGroup);//单选按钮设置在一组里
        }
        if (defalutSelection != null) {
            toggleGroup.selectToggle(radioButtons[defalutSelection]);//默认先选中哪个
        }
        return radioButtons;
    }
}
