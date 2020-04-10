package controller;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import util.AddNodeForPane;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonTrainerView;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 为了可以初始化，所以继承接口Initializable.
 * 为了使代码简洁，CRUD使用了自定义的工具类AddNodeForPane。
 */
public class DragonMomController implements Initializable {
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
     * 添加驯龙高手信息.
     */
    public void addDragonTrainer(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        String[] promptTexts = {"已存在的族群Id", "驯龙高手名字", "用户名", "密码"};
        TextField[] textFields = AddNodeForPane.addTextFieldForPane(vBox, promptTexts);

        //使用了自定义控件，弹出弹窗
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加驯龙高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            int dragonGroupId = Integer.parseInt(textFields[0].getText().trim());
            String name = textFields[1].getText();
            String username = textFields[2].getText().trim();
            String password = textFields[3].getText().trim();
            new DragonTrainerDAOImpl().save(dragonGroupId, name, username, password);//数据库保存数据

            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(username, password);
            TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);
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
            DragonTrainer trainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            if (trainer != null) {
                int dragonGroupId = trainer.getDragonGroupId();
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + trainer.getName(), "Id:" + dragonGroupId,
                        "族群名字:" + new DragonGroupDAOImpl().get(dragonGroupId).getName(),
                        "族群Id:" + trainer.getDragonGroupId(), "用户名:" + trainer.getUsername(),
                        "密码:" + trainer.getPassword()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("驯龙高手信息", vBox, "确定", null).showAndWait();
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
            DragonTrainer trainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            if (trainer != null) {
                GridPane gridPane = new GridPane();

                String[] labelTexts = {"名字:", "族群Id:", "用户名:", "密码:"};
                String[] textFiledContents = {trainer.getName(), String.valueOf(trainer.getDragonGroupId()),
                        trainer.getUsername(), trainer.getPassword()};
                TextField[] textFields = AddNodeForPane.addForGridPane(gridPane, labelTexts, textFiledContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("修改驯龙高手信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = textFields[0].getText().trim();
                    int dragonGroupId = Integer.parseInt(textFields[1].getText().trim());
                    String username = textFields[2].getText().trim();
                    String password = textFields[3].getText().trim();

                    new DragonTrainerDAOImpl().update(dragonTrainerId, dragonGroupId, name, username, password);

                    InitDragonTrainerView.flushTrainer(trainerTreeItemList, trainerRoot);
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
        VBox vBox = new VBox(10);

        String[] promptTexts = {"族群名字", "简介", "地理位置", "大小"};
        TextField[] textFields = AddNodeForPane.addTextFieldForPane(vBox, promptTexts);

        //使用了自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加族群高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = textFields[0].getText().trim();
            String profile = textFields[1].getText().trim();
            String location = textFields[2].getText().trim();
            double size = Double.parseDouble(textFields[3].getText().trim());
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
            DragonGroup group = new DragonGroupDAOImpl().get(dragonGroupId);
            if (group != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + group.getName(), "Id:" + group.getId(), "简介:" + group.getProfile(),
                        "地理位置:" + group.getLocation()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
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
            DragonGroup group = new DragonGroupDAOImpl().get(dragonGroupId);
            if (group != null) {
                GridPane gridPane = new GridPane();

                String[] labelTexts = {"名字:", "简介:", "地理位置:", "大小:"};
                String[] textFiledContents = {group.getName(), group.getProfile(), group.getLocation(),
                        String.valueOf(group.getSize())};
                TextField[] textFields = AddNodeForPane.addForGridPane(gridPane, labelTexts, textFiledContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("修改族群信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = textFields[0].getText().trim();
                    String profile = textFields[1].getText().trim();
                    String location = textFields[2].getText().trim();
                    double size = Double.parseDouble(textFields[3].getText().trim());

                    new DragonGroupDAOImpl().update(name, profile, location, size, dragonGroupId);

                    InitDragonGroupView.flushGroup(groupTreeItemList, groupRoot);
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
        String[] columnName = {"驯龙高手名字", "Id", "族群Id", "族群名字"};
        double[] columnPrefWidth = {150, 80, 80, 120};
        String[] columnId = {"name", "Id", "dragonGroupId", "dragonGroupName"};
        InitDragonTrainerView.initTrainerTreeTable(trainerTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTrainerTreeData() {
        InitDragonTrainerView.initTrainerTreeData(trainerTreeTableView, trainerRoot, trainerTreeItemList);
    }

    /**
     * 族群表：
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
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        InitDragonGroupView.initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

}
