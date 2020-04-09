package controller;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonTrainerView;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 为了可以初始化，所以继承该接口
 */
public class DragonMonController implements Initializable {
    @FXML
    TreeTableView<DragonTrainer> trainerTreeTableView;
    @FXML
    TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    TabPane tabPane;
    @FXML
    Button changeUser;

    TreeItem<DragonTrainer> trainerRoot = new TreeItem<DragonTrainer>(new DragonTrainer());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());

    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来.
     * 为表的更好地显示而加载。
     */
    List<TreeItem<DragonTrainer>> trainerTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 默认先显示驯龙高手的信息
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTrainerTreeTable();
        initTrainerTreeData();
        initGroupTreeTable();
        initGroupTreeData();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     */
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if (newTab.getText().equals("驯龙高手")) {
                    trainerTreeTableView.setVisible(true);
                    groupTreeTableView.setVisible(false);
                } else if (newTab.getText().equals("族群")) {
                    groupTreeTableView.setVisible(true);
                    trainerTreeTableView.setVisible(false);
                }
            }
        });
    }

    /**
     * 切换账号切换为登陆界面.
     */
    public void changeUser(ActionEvent actionEvent) {
        ChangeUser.changeUser(changeUser);
    }


    /**
     * 添加驯龙高手信息
     */
    public void addDragonTrainer(ActionEvent actionEvent) {
        VBox vBox = new VBox();

        TextField t_dragonGroupId = new TextField();
        t_dragonGroupId.setPromptText("已存在的族群Id");
        TextField t_name = new TextField();
        t_name.setPromptText("驯龙高手名字");
        TextField t_username = new TextField();
        t_username.setPromptText("用户名");
        TextField t_password = new TextField();
        t_password.setPromptText("密码");
        vBox.getChildren().addAll(t_name, t_dragonGroupId, t_username, t_password);

        vBox.setSpacing(10);

        //使用了自定义控件
        Dialog<ButtonType> dialog = DialogTool.dialog("添加驯龙高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText();
            int dragonGroupId = Integer.parseInt(t_dragonGroupId.getText().trim());
            String username = t_username.getText().trim();
            String password = t_password.getText().trim();
            new DragonTrainerDAOImpl().save(dragonGroupId, name, username, password);//数据库保存数据

            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(username, password);
            TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);//试下TreeItem后面加<>会怎么样
            trainerTreeItemList.add(treeItem);
            trainerRoot.getChildren().add(treeItem);
        }

    }

    /**
     * 删除驯龙高手信息.
     * 从treeItemList找到驯龙高手相匹配的treeItem,然后从树控件中移除
     */
    public void deleteDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("删除驯龙高手信息",
                "请输入驯龙高手的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonTrainerId = Integer.parseInt(result.get().trim());
            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            new DragonTrainerDAOImpl().delete(dragonTrainerId);

            for (TreeItem<DragonTrainer> treeItem : trainerTreeItemList) {
                if (treeItem.getValue().getDragonTrainerId() == dragonTrainerId) {
                    trainerTreeItemList.remove(treeItem);
                    trainerRoot.getChildren().remove(treeItem);
                    break;
                }
            }
        }
    }

    /**
     * 查询驯龙高手信息.
     */
    public void queryDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询驯龙高手信息",
                "请输入驯龙高手的Id", "Id:");
        if (result.isPresent()) {
            int dragonTrainerId = Integer.parseInt(result.get());
            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            if (dragonTrainer != null) {
                int dragonGroupId = dragonTrainer.getDragonGroupId();
                VBox vBox = new VBox();
                Text t_name = new Text("名字:" + dragonTrainer.getName());
                Text t_dragonTrainerId = new Text("Id:" + dragonGroupId);
                Text t_dragonGroupName = new Text("族群名字:" + new DragonGroupDAOImpl().get(dragonGroupId).getName());
                Text t_dragonGroupId = new Text("族群Id:" + dragonTrainer.getDragonGroupId());
                Text t_username = new Text("用户名:" + dragonTrainer.getUsername());
                Text t_password = new Text("密码:" + dragonTrainer.getPassword());
                vBox.getChildren().addAll(t_name, t_dragonTrainerId, t_dragonGroupName, t_dragonGroupId, t_username,
                        t_password);
                vBox.setSpacing(10);

                DialogTool.dialog("驯龙高手信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
            }
        }
    }

    /**
     * 修改驯龙高手信息.
     * 查询->显示原来信息->进行修改
     */
    public void changeDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog(null, "请输入驯龙高手的Id",
                "Id:");
        if (result.isPresent()) {
            int dragonTrainerId = Integer.parseInt(result.get());
            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            if (dragonTrainer != null) {
                GridPane gridPane = new GridPane();

                Label l_name = new Label("名字:");
                Label l_dragonGroupId = new Label("族群Id:");
                Label l_username = new Label("用户名:");
                Label l_password = new Label("密码:");

                TextField t_name = new TextField(dragonTrainer.getName());
                TextField t_dragonGroupId = new TextField(String.valueOf(dragonTrainer.getDragonGroupId()));
                TextField t_username = new TextField(dragonTrainer.getUsername());
                TextField t_password = new TextField(dragonTrainer.getPassword());

                gridPane.add(l_name, 0, 0);
                gridPane.add(t_name, 1, 0);
                gridPane.add(l_dragonGroupId, 0, 1);
                gridPane.add(t_dragonGroupId, 1, 1);
                gridPane.add(l_username, 0, 2);
                gridPane.add(t_username, 1, 2);
                gridPane.add(l_password, 0, 3);
                gridPane.add(t_password, 1, 3);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.dialog("修改驯龙高手信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    int dragonGroupId = Integer.parseInt(t_dragonGroupId.getText());
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = t_password.getText().trim();

                    new DragonTrainerDAOImpl().update(dragonTrainerId, dragonGroupId, name, username, password);

                    InitDragonTrainerView.flushTrainer(trainerTreeItemList,trainerRoot);
                }
            } else {
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
            }
        }
    }

    /**
     * 添加族群信息.
     */
    public void addDragonGroup(ActionEvent actionEvent) {
        VBox vBox = new VBox();

        TextField t_name = new TextField();
        t_name.setPromptText("族群名字");
        TextField t_profile = new TextField();
        t_profile.setPromptText("简介");
        TextField t_location = new TextField();
        t_location.setPromptText("地理位置");
        TextField t_size = new TextField();
        t_size.setPromptText("大小");
        vBox.getChildren().addAll(t_name, t_profile, t_location, t_size);

        vBox.setSpacing(10);

        //使用了自定义控件
        Dialog<ButtonType> dialog = DialogTool.dialog("添加族群高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText().trim();
            String profile = t_profile.getText().trim();
            String location = t_location.getText().trim();
            double size = Double.parseDouble(t_size.getText().trim());
            new DragonGroupDAOImpl().save(name, profile, location, size);

            DragonGroup dragonGroup = new DragonGroupDAOImpl().get(name);
            TreeItem<DragonGroup> treeItem = new TreeItem(dragonGroup);
            groupTreeItemList.add(treeItem);
            groupRoot.getChildren().add(treeItem);
        }

    }

    /**
     * 删除族群信息.
     * 注意要从treeItemList找到族群相匹配的treeItem,然后从树控件中移除
     */
    public void deleteDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("删除族群信息",
                "请输入族群的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonGroupId = Integer.parseInt(result.get().trim());
            DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
            new DragonGroupDAOImpl().delete(dragonGroupId);

            for (TreeItem<DragonGroup> treeItem : groupTreeItemList) {
                if (treeItem.getValue().getId() == dragonGroupId) {
                    groupTreeItemList.remove(treeItem);
                    groupRoot.getChildren().remove(treeItem);
                    break;
                }
            }
        }
    }

    /**
     * 查询族群信息.
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
                vBox.getChildren().addAll(t_name, t_Id, t_profile, t_location);
                vBox.setSpacing(10);

                DialogTool.dialog("族群信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
            }
        }
    }

    /**
     * 修改族群信息.
     * flushGroup()方法是为了刷新一下显示和groupTreeItemList
     */
    public void changeDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog(null, "请输入族群的Id",
                "Id:");
        if (result.isPresent()) {
            int dragonGroupId = Integer.parseInt(result.get());
            DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
            if (dragonGroup != null) {
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

                    InitDragonGroupView.flushGroup(groupTreeItemList,groupRoot);
                }
            } else {
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
            }
        }
    }


    /**
     * 驯龙高手表：
     * 设置列名、列宽
     * 调用工具类
     */
    public void initTrainerTreeTable() {
        String [] columnName = {"驯龙高手名字","Id","族群Id","族群名字"};
        double [] columnPrefWidth = {150,80,80,120};
        String [] columnId = {"name","Id","dragonGroupId","dragonGroupName"};
        InitDragonTrainerView.initTrainerTreeTable(trainerTreeTableView,columnName,columnPrefWidth,columnId);
    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTrainerTreeData() {
        InitDragonTrainerView.initTrainerTreeData(trainerTreeTableView,trainerRoot,trainerTreeItemList);
    }

    /**
     * 族群表：
     * 设置列名、列宽
     * 调用工具类
     */
    public void initGroupTreeTable() {
        String [] columnName = {"族群名字","Id","简介","地理位置","大小"};
        double [] columnPrefWidth = {120,80,120,120,80};
        String [] columnId = {"name","Id","profile","location","size"};
        InitDragonGroupView.initGroupTreeTable(groupTreeTableView,columnName,columnPrefWidth,columnId);
    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        InitDragonGroupView.initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

}
