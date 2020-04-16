package controller;

import entity.DragonGroup;
import entity.DragonMom;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.IDragonGroupDAO;
import model.IDragonMomDAO;
import model.IDragonTrainerDAO;
import model.ITicketDAO;
import util.*;
import util.table.DragonGroupTable;
import util.table.DragonTrainerTable;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.TextInputDialogTool;

import java.io.IOException;
import java.util.*;

/**
 * 为了可以初始化，所以继承接口Initializable.
 * 为了使代码简洁，CRUD使用了自定义的工具类AddNodeForPane。
 */
public class DragonMomController extends BaseController {
    @FXML
    private TreeTableView<DragonTrainer> trainerTreeTableView;
    @FXML
    private TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button changeUser;

    private DragonMom dragonMom = null;

    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    private IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    private ITicketDAO iTicketDAO = DAOFactory.getTicketDAOInstance();

    private TreeItem<DragonTrainer> trainerRoot = new TreeItem<DragonTrainer>(new DragonTrainer());

    private TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());

    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来.
     * 为表的更好地显示而加载。
     */
    private List<TreeItem<DragonTrainer>> trainerTreeItemList = new ArrayList<>();

    private List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 默认先显示驯龙高手的信息
     */
    public void init() {
        initTrainerTreeTable();
        initTrainerTreeData();
        initGroupTreeTable();
        initGroupTreeData();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     */
    @FXML
    @Override
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
    @FXML
    @Override
    public void switchAccount(ActionEvent actionEvent) {
        SwitchAccount.changeUser(changeUser);
    }


    /**
     * 添加驯龙高手信息.
     */
    public void addDragonTrainer(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        String[] promptTexts = {"已存在的族群Id", "驯龙高手名字", "用户名", "密码"};
        Map<String, TextField> map = PaneFilling.getInstance().addTextField(vBox, promptTexts);

        //使用了自定义控件，弹出弹窗
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加驯龙高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = map.get("驯龙高手名字").getText();
            String username = map.get("用户名").getText().trim();
            String password = map.get("密码").getText().trim();
            int dragonGroupId = 0;
            try {
                //判读输入的ID是否为整数
                dragonGroupId = Integer.parseInt(map.get("已存在的族群Id").getText().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "非法输入");
                return;
            }

            if (CheckValid.isEmpty(name, username, password, map.get("已存在的族群Id").getText().trim()) ||
                    !CheckValid.isValidUsername(username)) {
                //判断是否有空的信息以及用户名是否重复
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "信息填写不完整" +
                        "或者用户名已注册");
                return;
            }

            //数据库保存数据,items记录影响的数据条数
            int items = iDragonTrainerDAO.save(dragonGroupId, name, username, password);

            if (items == 0) {//说明没有插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是族群不存在或者用户名已注册");
            } else {
                DragonTrainer dragonTrainer = iDragonTrainerDAO.get(username, password);
                TreeItem<DragonTrainer> treeItem = new TreeItem<>(dragonTrainer);
                trainerTreeItemList.add(treeItem);
                trainerRoot.getChildren().add(treeItem);
            }
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
            int dragonTrainerId = 0;
            try {
                dragonTrainerId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            DragonTrainer dragonTrainer = iDragonTrainerDAO.get(dragonTrainerId);
            int items = iDragonTrainerDAO.delete(dragonTrainerId);

            if (items == 0) {//说明没有数据删除
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "可能是没有与id匹配的驯龙高手");
            } else {
                for (TreeItem<DragonTrainer> treeItem : trainerTreeItemList) {
                    if (treeItem.getValue().getDragonTrainerId() == dragonTrainerId) {
                        trainerTreeItemList.remove(treeItem);
                        trainerRoot.getChildren().remove(treeItem);
                        break;
                    }
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
            int dragonTrainerId = 0;
            try {
                dragonTrainerId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            DragonTrainer trainer = iDragonTrainerDAO.get(dragonTrainerId);
            if (trainer != null) {
                int dragonGroupId = trainer.getDragonGroupId();
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + trainer.getName(), "Id:" + dragonGroupId,
                        "族群名字:" + iDragonTrainerDAO.get(dragonGroupId).getName(),
                        "族群Id:" + trainer.getDragonGroupId(), "用户名:" + trainer.getUsername(),
                        "密码:" + trainer.getPassword()};
                PaneFilling.getInstance().addText(vBox, textContents);

                DialogTool.showDialog("驯龙高手信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
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
            int dragonTrainerId = 0;

            try {
                dragonTrainerId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            DragonTrainer trainer = iDragonTrainerDAO.get(dragonTrainerId);
            if (trainer != null) {
                GridPane gridPane = new GridPane();

                String[] labelTexts = {"名字:", "族群Id:", "密码:"};
                String[] textFiledContents = {trainer.getName(), String.valueOf(trainer.getDragonGroupId()),
                        trainer.getUsername(), trainer.getPassword()};
                Map<String, TextField> map = PaneFilling.getInstance().addForGridPane(gridPane, labelTexts, textFiledContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("修改驯龙高手信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = map.get("名字:").getText().trim();
                    String password = map.get("密码:").getText().trim();
                    int dragonGroupId = 0;
                    try {
                        //输入的ID是否为整数
                        dragonGroupId = Integer.parseInt(map.get("族群Id:").getText().trim());
                    } catch (Exception e) {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                        return;
                    }

                    if (CheckValid.isEmpty(name, password, map.get("族群Id:").getText().trim())) {
                        //判断是否有空的信息
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                        return;
                    }

                    int items = iDragonTrainerDAO.update(dragonTrainerId, dragonGroupId, name, password);

                    if (items == 0) {//说明没有数据修改
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "可能族群不存在");
                    } else {
                        DragonTrainerTable.getInstance().flushTrainer(trainerTreeItemList, trainerRoot);
                    }
                }
            } else {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
            }
        }
    }

    /**
     * 添加族群信息.
     */
    public void addDragonGroup(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        String[] promptTexts = {"族群名字", "简介", "地理位置", "大小"};
        Map<String, TextField> map = PaneFilling.getInstance().addTextField(vBox, promptTexts);

        //使用了自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加族群高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = map.get("族群名字").getText().trim();
            String profile = map.get("简介").getText().trim();
            String location = map.get("地理位置").getText().trim();
            double size = 0;
            try {
                size = Double.parseDouble(map.get("大小").getText().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            if (CheckValid.isEmpty(name, profile, location, map.get("地理位置").getText().trim())) {
                //判断是否有空的信息
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                return;
            }

            int items = iDragonGroupDAO.save(name, profile, location, size);

            if (items == 0) {//说明没有插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是该名字已存在");
            } else {
                DragonGroup dragonGroup = iDragonGroupDAO.get(name);
                TreeItem<DragonGroup> treeItem = new TreeItem<>(dragonGroup);
                groupTreeItemList.add(treeItem);
                groupRoot.getChildren().add(treeItem);
            }
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
            int dragonGroupId = 0;
            try {
                dragonGroupId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            DragonGroup dragonGroup = iDragonGroupDAO.get(dragonGroupId);
            int items = iDragonGroupDAO.delete(dragonGroupId);

            if (items == 0) {//说明没有数据删除
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "可能是没有与id匹配的族群");
            } else {
                for (TreeItem<DragonGroup> treeItem : groupTreeItemList) {
                    if (treeItem.getValue().getId() == dragonGroupId) {
                        groupTreeItemList.remove(treeItem);
                        groupRoot.getChildren().remove(treeItem);
                        break;
                    }
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
            int dragonGroupId = 0;
            try {
                dragonGroupId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            DragonGroup group = iDragonGroupDAO.get(dragonGroupId);
            if (group != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + group.getName(), "Id:" + group.getId(), "简介:" + group.getProfile(),
                        "地理位置:" + group.getLocation()};
                PaneFilling.getInstance().addText(vBox, textContents);

                DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
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
            int dragonGroupId = 0;
            try {
                dragonGroupId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            DragonGroup group = iDragonGroupDAO.get(dragonGroupId);
            if (group != null) {
                GridPane gridPane = new GridPane();

                String[] labelTexts = {"名字:", "简介:", "地理位置:", "大小:"};
                String[] textFiledContents = {group.getName(), group.getProfile(), group.getLocation(),
                        String.valueOf(group.getSize())};
                Map<String, TextField> map = PaneFilling.getInstance().addForGridPane(gridPane, labelTexts, textFiledContents);

                gridPane.setVgap(10);

                Optional<ButtonType> choice = DialogTool.showDialog("修改族群信息", gridPane, "确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String name = map.get("名字:").getText().trim();
                    String profile = map.get("简介:").getText().trim();
                    String location = map.get("地理位置:").getText().trim();
                    double size = 0;
                    try {
                        size = Double.parseDouble(map.get("大小:").getText().trim());
                    } catch (Exception e) {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                        return;
                    }

                    if (CheckValid.isEmpty(name, profile, location, map.get("大小:").getText().trim())) {
                        //判断是否有空的信息
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                        return;
                    }

                    int items = iDragonGroupDAO.update(name, profile, location, size, dragonGroupId);

                    if (items == 0) {//说明没有数据修改
                        AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "可能是该名字已存在");
                    } else {
                        DragonGroupTable.getInstance().flushGroup(groupTreeItemList, groupRoot);
                    }
                }
            } else {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
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
        DragonTrainerTable.getInstance().initTrainerTreeTable(trainerTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTrainerTreeData() {
        DragonTrainerTable.getInstance().initTrainerTreeData(trainerTreeTableView, trainerRoot, trainerTreeItemList);
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
        DragonGroupTable.getInstance().initGroupTreeTable(groupTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        DragonGroupTable.getInstance().initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

    /**
     * 点击事件，弹出弹窗显示金库中的钱.
     */
    public void showMoneyTub(ActionEvent actionEvent) {
        dragonMom = iDragonMomDAO.get();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(25));
        Text text = new Text("金库余额:" + dragonMom.getMoneyTub());
        text.setFont(new Font(15));
        vBox.getChildren().add(text);
        vBox.setAlignment(Pos.CENTER);

        DialogTool.showDialog("金库", vBox, "确定",
                null).showAndWait();
    }

    public void setDragonMom(DragonMom dragonMom) {
        this.dragonMom = dragonMom;
    }

    /**
     * 点击事件，处理外邦人的退票处理.
     */
    public void dealBackTickets(ActionEvent actionEvent) {
        try {
            ViewManager.openView(ViewManager.backTicketsUrl, null, 400.0, 400.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
