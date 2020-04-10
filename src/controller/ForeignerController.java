package controller;

import entity.Dragon;
import entity.DragonGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import util.AddNodeForPane;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonView;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * 外邦人的控制器，实现Initializable接口来初始化.
 * 为了使代码简洁，查询方法使用了自定义的工具类AddNodeForPane。
 * */
public class ForeignerController implements Initializable {
    @FXML
    TreeTableView<Dragon> dragonTreeTableView;
    @FXML
    TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    TabPane tabPane;
    @FXML
    Button changeUser;

    TreeItem<Dragon> dragonRoot = new TreeItem<Dragon>(new Dragon());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());


    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来.
     * 为了表的显示而加载。
     */
    List<TreeItem<Dragon>> dragonTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 初始化，默认先显示龙的表.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDragonTreeTable();
        initDragonTreeData();
        initGroupTreeTable();
        initGroupTreeData();

        tabPaneListener();

    }

    /**
     * TabPane监听器，用户点击不同的Tab则切换不同的表的信息
     */
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if (newTab.getText().equals("龙")) {
                    dragonTreeTableView.setVisible(true);
                    groupTreeTableView.setVisible(false);
                } else if (newTab.getText().equals("族群")) {
                    dragonTreeTableView.setVisible(false);
                    groupTreeTableView.setVisible(true);
                }
            }
        });
    }

    /**
     * 切换用户.
     */
    public void changeUser(ActionEvent actionEvent) {
        ChangeUser.changeUser(changeUser);
    }

    /**
     * 通过id来查询龙的信息.
     * 外邦人看不到属性:年龄。
     */
    public void queryDragon(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询龙的信息",
                "请输入龙的Id", "Id:");
        if (result.isPresent()) {
            int dragonId = Integer.parseInt(result.get());
            Dragon dragon = new DragonDAOImpl().get(dragonId);
            if (dragon != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"龙的Id:" + dragon.getDragonId(), "名字:" + dragon.getName(),
                        "性别:" + dragon.getSex(), "简介:" + dragon.getProfile(), "是否在训练:" + dragon.isTraining(),
                        "是否健康:" + dragon.isHealthy()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("龙的信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该龙的信息");
            }
        }
    }

    /**
     * 对族群的信息进行查询.
     * 外邦人看不到属性:地理位置。
     */
    public void queryDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询族群信息",
                "请输入族群的Id", "Id:");
        if (result.isPresent()) {
            int dragonGroupId = Integer.parseInt(result.get());
            DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
            if (dragonGroup != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + dragonGroup.getName(), "Id:" + dragonGroup.getId(),
                        "简介:" + dragonGroup.getProfile(), "大小:" + dragonGroup.getSize()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
            }
        }
    }

    /**
     * 族群表.
     * 设置列名、列宽
     * 调用工具类
     * 外邦人看不到族群的Id属性、地理位置
     */
    public void initGroupTreeTable() {
        String[] columnName = {"族群名字", "Id", "简介", "大小"};
        double[] columnPrefWidth = {120, 80, 120, 120, 80};
        String[] columnId = {"name", "Id", "profile", "size"};
        InitDragonGroupView.initGroupTreeTable(groupTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 族群表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        InitDragonGroupView.initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

    /**
     * 龙表.
     * 设置列名、列宽
     * 调用工具类
     * 外邦人看不到龙的年龄属性。
     */
    public void initDragonTreeTable() {
        String[] columnName = {"名字", "Id", "性别", "简介", "训练", "健康"};
        double[] columnPrefWidth = {120, 80, 80, 120, 80, 80};
        String[] columnId = {"name", "Id", "sex", "profile", "training", "healthy"};
        InitDragonView.initDragonTreeTable(dragonTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 龙表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initDragonTreeData() {
        InitDragonView.initDragonTreeData(dragonTreeTableView, dragonRoot, dragonTreeItemList);
    }

}
