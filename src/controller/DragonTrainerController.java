package controller;

import entity.Dragon;
import entity.DragonGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.IDragonDAO;
import model.IDragonGroupDAO;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import util.AddNodeForPane;
import util.DAOFactory;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonView;
import widget.AlertTool;
import widget.DialogTool;
import widget.SingleSelectionTool;
import widget.TextInputDialogTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 驯龙高手的控制器.
 * 为了使代码简洁，CRUD使用了自定义的工具类AddNodeForPane。
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

    IDragonDAO iDragonDAO = DAOFactory.getDragonDAOInstance();

    IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

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
     */
    public void initText() {
        DragonGroup dragonGroup = new DragonGroupDAOImpl().get(dragonGroupId);
        text1.setText("\n族群ID: " + dragonGroup.getId() + "\n\n" + "族群名字: " + dragonGroup.getName());
        text2.setText("\n族群ID: " + dragonGroup.getId() + "\n\n" + "族群名字: " + dragonGroup.getName());
    }

    /**
     * 为族群添加龙.
     * 刚添加龙的时候，默认龙是健康的，且不在训练状态。
     * 使用了自己封转好的单选框来选择龙的性别。
     */
    public void addDragon(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        String[] promotTexts = {"龙的名字", "龙的简介", "年龄"};
        TextField[] textFields = AddNodeForPane.addTextFieldForPane(vBox, promotTexts);

        //自定义的单选框，选择龙的性别
        HBox h_sex = new HBox(10);
        String[] buttonName = {"雄性", "雌性"};
        RadioButton[] radioButtons = SingleSelectionTool.singSelection(h_sex, buttonName, 0);
        h_sex.getChildren().addAll(radioButtons[0], radioButtons[1]);


        vBox.getChildren().add(h_sex);

        Dialog<ButtonType> dialog = DialogTool.showDialog("龙的信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            //从信息框中得到信息并存入数据库
            String name = textFields[0].getText();
            String profile = textFields[1].getText().trim();
            int age = Integer.parseInt(textFields[2].getText().trim());
            String sex = null;
            if (radioButtons[0].isSelected()) {
                sex = radioButtons[0].getText();
            } else if (radioButtons[1].isSelected()) {
                sex = radioButtons[1].getText();
            }
            int items = iDragonDAO.save(dragonGroupId, name, profile, false, true, sex, age);//数据库保存数据

            if(items == 0){//说明没有插入数据
                AlertTool.alert(Alert.AlertType.WARNING,"错误","添加失败","可能是该名字已存在");
            }else{
                //通过族群id和名字来获取龙的实例
                Dragon dragon = iDragonDAO.get(dragonGroupId, name);
                TreeItem<Dragon> treeItem = new TreeItem<>(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }

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
            Dragon dragon = iDragonDAO.get(dragonId);
            int items = iDragonDAO.delete(dragonId);

            if(items == 0){
                AlertTool.alert(Alert.AlertType.WARNING,"错误","删除失败","可能是没有与id匹配的龙");
            }else{
                for (TreeItem<Dragon> treeItem : dragonTreeItemList) {
                    if (treeItem.getValue().getDragonId() == dragonId) {
                        dragonTreeItemList.remove(treeItem);
                        dragonRoot.getChildren().remove(treeItem);
                        break;
                    }
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
            Dragon dragon = iDragonDAO.get(dragonId);
            if (dragon != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"龙的Id:" + dragon.getDragonId(), "名字:" + dragon.getName(),
                        "性别:" + dragon.getSex(), "年龄:" + dragon.getAge(), "简介:" + dragon.getProfile(),
                        "是否在训练:" + dragon.isTraining(), "是否健康:" + dragon.isHealthy()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("龙的信息", vBox, "确定", null).showAndWait();
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
            Dragon dragon = iDragonDAO.get(dragonId);
            boolean dragonTraining = dragon.isTraining();//标记龙未修改前的训练状态
            boolean dragonHealthy = dragon.isHealthy();//标记龙未修改前的健康状态
            if (dragon != null) {
                GridPane gridPane = new GridPane();

                //先给GridPane添加一些Label和TextField
                String[] labelTexts = {"名字:", "年龄", "简介:"};
                String[] textFiledContents = {dragon.getName(), String.valueOf(dragon.getAge()), dragon.getProfile()};
                TextField[] textFields = AddNodeForPane.addForGridPane(gridPane, labelTexts, textFiledContents);

                Label l_training = new Label("训练中:");
                Label l_healthy = new Label("健康:");

                //自定义的单选框，选择龙的训练状态
                HBox h_training = new HBox(8);
                String[] buttonName = {"true", "false"};
                RadioButton[] trainButtons = SingleSelectionTool.singSelection(h_training, buttonName, dragonTraining ? 1 : 0);
                h_training.getChildren().addAll(trainButtons[0], trainButtons[1]);

                //自定义的单选框，选择龙的健康状态
                HBox h_healthy = new HBox(8);
                RadioButton[] healthyButtons = SingleSelectionTool.singSelection(h_training, buttonName, dragonHealthy ? 1 : 0);
                h_healthy.getChildren().addAll(healthyButtons[0], healthyButtons[1]);

                //给GridPane添加单选框
                gridPane.add(l_training, 0, 3);
                gridPane.add(h_training, 1, 3);
                gridPane.add(l_healthy, 0, 4);
                gridPane.add(h_healthy, 1, 4);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("修改龙的信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    //从弹框得到信息并保存进数据库
                    String name = textFields[0].getText().trim();
                    int age = Integer.parseInt(textFields[1].getText().trim());
                    String profile = textFields[2].getText().trim();
                    boolean training;
                    boolean healthy;
                    if (trainButtons[0].isSelected()) {
                        training = true;
                    } else {
                        training = false;
                    }
                    if (healthyButtons[0].isSelected()) {
                        healthy = true;
                    } else {
                        healthy = false;
                    }

                    int items = iDragonDAO.update(dragonId, dragonGroupId, name, profile, training, healthy, age);

                    if(items == 0){//说明没有数据修改
                        AlertTool.alert(Alert.AlertType.WARNING,"错误","修改失败","可能是该名字已存在");
                    }else{
                        InitDragonView.flushDragon(dragonTreeItemList, dragonRoot, dragonGroupId);
                    }
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
            DragonGroup group = iDragonGroupDAO.get(dragonGroupId);
            if (group != null) {
                VBox vBox = new VBox(10);

                String[] promptTexts = {"名字:" + group.getName(), "Id:" + group.getId(), "简介:" + group.getProfile(),
                        "地理位置:" + group.getLocation(), "大小:" + group.getSize()};
                AddNodeForPane.addTextForPane(vBox, promptTexts);

                DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
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
        DragonGroup group = iDragonGroupDAO.get(dragonGroupId);

        GridPane gridPane = new GridPane();

        String[] labelTexts = {"名字:", "简介:", "地理位置:", "大小:"};
        String[] textFieldContents = {group.getName(), group.getProfile(), group.getLocation(), String.valueOf(group.getSize())};
        TextField[] textFields = AddNodeForPane.addForGridPane(gridPane, labelTexts, textFieldContents);

        gridPane.setVgap(10);

        Optional<ButtonType> choice = DialogTool.showDialog("修改族群信息", gridPane, "确定",
                null).showAndWait();

        if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = textFields[0].getText().trim();
            String profile = textFields[1].getText().trim();
            String location = textFields[2].getText().trim();
            double size = Double.parseDouble(textFields[3].getText().trim());

            int items = iDragonGroupDAO.update(name, profile, location, size, dragonGroupId);

            if(items == 0){//说明没有数据修改
                AlertTool.alert(Alert.AlertType.WARNING,"错误","修改失败","可能是该名字已存在");
            }else{
                InitDragonGroupView.flushGroup(groupTreeItemList, groupRoot);
            }
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
