package controller;

import entity.Dragon;
import entity.DragonGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonView;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 驯龙高手的控制器.
 */
public class DragonTrainerController {
    @FXML
    TreeTableView<Dragon> dragonTreeTableView;
    @FXML
    TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    TabPane tabPane;
    @FXML
    Button changeUser;
    @FXML
    Text text1;
    @FXML
    Text text2;

    /**
     * 记录驯龙高手所在族群的Id.
     */
    private int dragonGroupId;


    TreeItem<Dragon> dragonRoot = new TreeItem<Dragon>(new Dragon());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());


    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来.
     * 为了表的显示而加载。
     */
    List<TreeItem<Dragon>> dragonTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 初始化.
     * 默认先显示龙的信息。
     */
    public void Init() {
        initDragonTreeTable();
        initDragonTreeData();
        initGroupTreeTable();
        initGroupTreeData();
        initText();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Tab则切换不同的表的信息
     */
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if (newTab.getText().equals("族群的龙")) {
                    dragonTreeTableView.setVisible(true);
                    groupTreeTableView.setVisible(false);
                } else if (newTab.getText().equals("所有族群")) {
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
     * 初始化Text控件.
     * 该控件显示族群的id和名字，那么就不用额外在列表里显示族群的有关信息了。
     * */
    public void initText(){
        DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
        text1.setText("\n族群ID: "+dragonGroup.getId()+"\n\n"+ "族群名字: "+dragonGroup.getName());
        text2.setText("\n族群ID: "+dragonGroup.getId()+"\n\n"+ "族群名字: "+dragonGroup.getName());
    }

    /**
     * 为族群添加龙.
     */
    public void addDragon(ActionEvent actionEvent) {
        VBox vBox = new VBox();

        TextField t_name = new TextField();
        t_name.setPromptText("龙的名字");
        TextField t_profile = new TextField();
        t_profile.setPromptText("龙的简介");
        TextField t_sex = new TextField();
        t_sex.setPromptText("性别");
        TextField t_age = new TextField();
        t_age.setPromptText("年龄");
        vBox.getChildren().addAll(t_name, t_profile, t_sex, t_age);

        vBox.setSpacing(10);

        Dialog<ButtonType> dialog = DialogTool.dialog("龙的信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText();
            String profile = t_profile.getText().trim();
            String sex = t_sex.getText().trim();
            int age = Integer.parseInt(t_age.getText().trim());
            new DragonDAOImpl().save(dragonGroupId, name, profile, false, true, sex, age);//数据库保存数据

            //通过族群id和名字来获取龙的实例
            Dragon dragon = new DragonDAOImpl().get(dragonGroupId, name);
            TreeItem<Dragon> treeItem = new TreeItem(dragon);//试下TreeItem后面加<>会怎么样
            dragonTreeItemList.add(treeItem);
            dragonRoot.getChildren().add(treeItem);

            InitDragonView.flushDragon(dragonTreeItemList, dragonRoot, dragonGroupId);//刷新一下表的显示
        }
    }

    /**
     * 为族群删除龙.
     * 注意要从treeItemList找到族群相匹配的treeItem,然后从树控件中移除
     */
    public void deleteDragon(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("删除龙的信息",
                "请输入龙的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonId = Integer.parseInt(result.get().trim());
            Dragon dragon = new DragonDAOImpl().get(dragonId);
            new DragonDAOImpl().delete(dragonId);

            for (TreeItem<Dragon> treeItem : dragonTreeItemList) {
                if (treeItem.getValue().getDragonId() == dragonId) {
                    dragonTreeItemList.remove(treeItem);
                    dragonRoot.getChildren().remove(treeItem);
                    break;
                }
            }
        }
    }

    /**
     * 查询龙的信息.
     */
    public void queryDragon(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询龙的信息",
                "请输入龙的Id", "Id:");
        if (result.isPresent()) {
            int dragonId = Integer.parseInt(result.get());
            Dragon dragon = new DragonDAOImpl().get(dragonId);
            if (dragon != null) {
                VBox vBox = new VBox();
                Text t_Id = new Text("龙的Id:" + dragon.getDragonId());
                Text t_name = new Text("名字:" + dragon.getName());
                Text t_sex = new Text("性别:" + dragon.getSex());
                Text t_age = new Text("年龄:" + dragon.getAge());
                Text t_profile = new Text("简介:" + dragon.getProfile());
                Text t_training = new Text("是否在训练:" + dragon.isTraining());
                Text t_healthy = new Text("是否健康:" + dragon.isHealthy());
                vBox.getChildren().addAll(t_Id, t_name, t_sex, t_age, t_profile, t_training, t_healthy);
                vBox.setSpacing(10);

                DialogTool.dialog("龙的信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该龙的信息");
            }
        }
    }

    /**
     * 修改龙的信息.
     * 设置为性别不可改动。
     */
    public void changeDragon(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog(null, "请输入龙的Id",
                "Id:");
        if (result.isPresent()) {
            int dragonId = Integer.parseInt(result.get());
            Dragon dragon = new DragonDAOImpl().get(dragonId);
            if (dragon != null) {
                GridPane gridPane = new GridPane();

                Label l_name = new Label("名字:");
                Label l_age = new Label("年龄");
                Label l_profile = new Label("简介:");
                Label l_training = new Label("训练中:");
                Label l_healthy = new Label("健康:");

                TextField t_name = new TextField(dragon.getName());
                TextField t_age = new TextField(String.valueOf(dragon.getAge()));
                TextField t_profile = new TextField(dragon.getProfile());
                TextField t_training = new TextField(String.valueOf(dragon.isTraining()));
                TextField t_healthy = new TextField(String.valueOf(dragon.isHealthy()));

                gridPane.add(l_name, 0, 0);
                gridPane.add(t_name, 1, 0);
                gridPane.add(l_age, 0, 1);
                gridPane.add(t_age, 1, 1);
                gridPane.add(l_profile, 0, 2);
                gridPane.add(t_profile, 1, 2);
                gridPane.add(l_training, 0, 3);
                gridPane.add(t_training, 1, 3);
                gridPane.add(l_healthy, 0, 4);
                gridPane.add(t_healthy, 1, 4);


                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.dialog("修改龙的信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = t_name.getText().trim();
                    int age = Integer.parseInt(t_age.getText().trim());
                    String profile = t_profile.getText().trim();
                    boolean training = t_training.getText().trim().equals("true");
                    boolean healthy = t_healthy.getText().trim().equals("true");

                    new DragonDAOImpl().update(dragonId, dragonGroupId, name, profile, training, healthy, age);

                    InitDragonView.flushDragon(dragonTreeItemList, dragonRoot, dragonGroupId);
                }
            } else {
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该龙的信息");
            }
        }

    }

    /**
     * 可以查询所有族群的信息.
     */
    public void queryDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询族群信息",
                "请输入族群的Id", "Id:");
        if (result.isPresent()) {
            int dragonGroupId = Integer.parseInt(result.get());
            DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
            if (dragonGroup != null) {
                VBox vBox = new VBox();
                Text t_name = new Text("名字:" + dragonGroup.getName());
                Text t_Id = new Text("Id:" + dragonGroup.getId());
                Text t_profile = new Text("简介:" + dragonGroup.getProfile());
                Text t_location = new Text("地理位置:" + dragonGroup.getLocation());
                Text t_size = new Text("大小:" + dragonGroup.getSize());
                vBox.getChildren().addAll(t_name, t_Id, t_profile, t_location,t_size);
                vBox.setSpacing(10);

                DialogTool.dialog("族群信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
            }
        }
    }

    /**
     * 对我的族群信息进行修改.
     */
    public void changeDragonGroup(ActionEvent actionEvent) {
        DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);

        GridPane gridPane = new GridPane();

        Label l_name = new Label("名字:");
        Label l_profile = new Label("简介:");
        Label l_location = new Label("地理位置:");
        Label l_size = new Label("大小:");

        TextField t_name = new TextField(dragonGroup.getName());
        TextField t_profile = new TextField(dragonGroup.getProfile());
        TextField t_location = new TextField(dragonGroup.getLocation());
        TextField t_size = new TextField(String.valueOf(dragonGroup.getSize()));

        gridPane.add(l_name, 0, 0);
        gridPane.add(t_name, 1, 0);
        gridPane.add(l_profile, 0, 1);
        gridPane.add(t_profile, 1, 1);
        gridPane.add(l_location, 0, 2);
        gridPane.add(t_location, 1, 2);
        gridPane.add(l_size, 0, 3);
        gridPane.add(t_size, 1, 3);

        gridPane.setVgap(10);

        Optional<ButtonType> choice = DialogTool.dialog("修改族群信息", gridPane, "确定",
                null).showAndWait();

        if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText().trim();
            String profile = t_profile.getText().trim();
            String location = t_location.getText().trim();
            double size = Double.parseDouble(t_size.getText().trim());

            new DragonGroupDAOImpl().update(name, profile, location, size, dragonGroupId);

            InitDragonGroupView.flushGroup(groupTreeItemList, groupRoot);
        }
    }

    /**
     * 族群表.
     * 设置列名、列宽
     * 调用工具类
     */
    public void initGroupTreeTable() {
        String[] columnName = {"族群名字", "Id", "简介", "地理位置", "大小"};
        double[] columnPrefWidth = {120, 80, 120, 120, 80};
        String[] columnId = {"name", "Id", "profile", "location", "size"};
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
     */
    public void initDragonTreeTable() {
        String[] columnName = {"Id", "名字", "性别", "年龄", "简介", "训练", "健康"};
        double[] columnPrefWidth = {80, 120, 80, 80, 120, 80, 80};
        String[] columnId = {"Id", "name", "sex", "age", "profile", "training", "healthy"};
        InitDragonView.initDragonTreeTable(dragonTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 龙表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initDragonTreeData() {
        InitDragonView.initDragonTreeData(dragonTreeTableView, dragonRoot, dragonTreeItemList, dragonGroupId);
    }

    /**
     * 设置驯龙高手所在族群的id.
     */
    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }

}
