package controller;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

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
    TreeTableView treeTableView;
    @FXML
    TabPane tabPane;

    TreeItem<DragonTrainer> trainerRoot = new TreeItem<DragonTrainer>(new DragonTrainer());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());
    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来
     */
    List<TreeItem<DragonTrainer>> trainerTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 默认先显示驯龙高手的信息
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTrainerTreeTable();//默认先显示驯龙高手的信息
        initTrainerTreeData();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     */
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                System.out.println(t1.getText());
            }
        });
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
     * 删除驯龙高手信息
     */
    public void deleteDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("删除驯龙高手信息",
                "请输入驯龙高手的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonTrainerId = Integer.parseInt(result.get().trim());
            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            new DragonTrainerDAOImpl().delete(dragonTrainerId);
            /**
             * 从treeItemList找到驯龙高手相匹配的treeItem,然后从树控件中移除
             * */
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
     * 查询驯龙高手信息
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

                DialogTool.dialog("驯龙高手信息",vBox,"确定",null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
            }
        }
    }

    /**
     * 修改驯龙高手信息
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

                Optional<ButtonType> choice = DialogTool.dialog("修改驯龙高手信息",gridPane,"确定",
                        null).showAndWait();

                if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    int dragonGroupId = Integer.parseInt(t_dragonGroupId.getText());
                    String name = t_name.getText().trim();
                    String username = t_username.getText().trim();
                    String password = t_password.getText().trim();

                    new DragonTrainerDAOImpl().update(dragonTrainerId, dragonGroupId, name, username, password);

                    for (TreeItem<DragonTrainer> treeItem : trainerTreeItemList) {

                        DragonTrainer dragonTrainer1 = treeItem.getValue();
                        /**
                         * 完成2步。
                         * 1.treeItemList的treeItem要更换
                         * 2.树控件中的显示要更换
                         * */
                        if (dragonTrainer1.getDragonTrainerId() == dragonTrainerId) {
                            dragonTrainer1.setDragonGroupId(dragonGroupId);
                            dragonTrainer1.setName(name);
                            dragonTrainer1.setUsername(username);
                            dragonTrainer1.setPassword(password);

                            TreeItem<DragonTrainer> treeItem1 = new TreeItem<>(dragonTrainer1);
                            trainerTreeItemList.remove(treeItem);
                            trainerTreeItemList.add(treeItem1);

                            trainerRoot.getChildren().remove(treeItem);
                            trainerRoot.getChildren().add(treeItem1);
                            break;
                        }
                    }
                }
            } else {
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
            }
        }
    }

    /**
     * 驯龙高手表：
     * 设置列名、列宽
     * */
    public void initTrainerTreeTable() {
        //这里添加了多个列
        TreeTableColumn<DragonTrainer, DragonTrainer> columns[] = new TreeTableColumn[4];
        columns[0] = new TreeTableColumn("驯龙高手名字");
        columns[1] = new TreeTableColumn("Id");
        columns[2] = new TreeTableColumn("族群Id");
        columns[3] = new TreeTableColumn("族群名字");
        treeTableView.getColumns().addAll(columns);

        Callback cellValueFactory = new Callback() {
            @Override
            public Object call(Object o) {
                TreeTableColumn.CellDataFeatures p = (TreeTableColumn.CellDataFeatures) o;
                return p.getValue().valueProperty();
            }
        };
        for (int i = 0; i < columns.length; i++) {
            columns[i].setCellValueFactory(cellValueFactory);
        }

        //设置列的宽度
        columns[0].setPrefWidth(150);
        columns[1].setPrefWidth(80);
        columns[2].setPrefWidth(80);
        columns[3].setPrefWidth(120);

        //设置CellFactory,定义每一列单元格的显示
        columns[0].setCellFactory((param) -> {
            return new TrainerTableTreeCell("name");
        });
        columns[1].setCellFactory((param) -> {
            return new TrainerTableTreeCell("Id");
        });
        columns[2].setCellFactory((param) -> {
            return new TrainerTableTreeCell("dragonGroupId");
        });
        columns[3].setCellFactory((param) -> {
            return new TrainerTableTreeCell("dragonGroupName");
        });
    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     * */
    public void initTrainerTreeData() {
        treeTableView.setRoot(trainerRoot);
        treeTableView.setShowRoot(false);

        List<DragonTrainer> dragonTrainerList = new DragonTrainerDAOImpl().getList();
        if (dragonTrainerList != null) {
            for (DragonTrainer dragonTrainer : dragonTrainerList) {
                TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);
                trainerTreeItemList.add(treeItem);
                trainerRoot.getChildren().add(treeItem);
            }
        }
    }

    /**
     * 驯龙高手表：
     * 单元格的显示
     * */
    class TrainerTableTreeCell extends TreeTableCell<DragonTrainer, DragonTrainer> {
        String columnID;

        public TrainerTableTreeCell(String columnID) {
            this.columnID = columnID;
        }

        @Override
        protected void updateItem(DragonTrainer item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);
                if (columnID.equals("name")) {
                    this.setText(item.getName());
                } else if (columnID.equals("Id")) {
                    this.setText(String.valueOf(item.getDragonTrainerId()));
                } else if (columnID.equals("dragonGroupId")) {
                    this.setText(String.valueOf(item.getDragonGroupId()));
                } else if (columnID.equals("dragonGroupName")) {
                    int dragonGroupId = item.getDragonGroupId();
                    //获得族群名字
                    String dragonGroupName = new DragonGroupDAOImpl().get(dragonGroupId).getName();
                    this.setText(dragonGroupName);
                }
            }
        }
    }

//    /**
//     * 族群表：
//     * 设置列名、列宽
//     * */
//    public void initGroupTreeTable() {
//        //这里添加了多个列
//        TreeTableColumn<DragonGroup, DragonGroup> columns[] = new TreeTableColumn[5];
//        columns[0] = new TreeTableColumn("族群名字");
//        columns[1] = new TreeTableColumn("Id");
//        columns[2] = new TreeTableColumn("简介");
//        columns[3] = new TreeTableColumn("地理位置");
//        columns[4] = new TreeTableColumn("大小");
//        treeTableView.getColumns().addAll(columns);
//
//        Callback cellValueFactory = new Callback() {
//            @Override
//            public Object call(Object o) {
//                TreeTableColumn.CellDataFeatures p = (TreeTableColumn.CellDataFeatures) o;
//                return p.getValue().valueProperty();
//            }
//        };
//        for (int i = 0; i < columns.length; i++) {
//            columns[i].setCellValueFactory(cellValueFactory);
//        }
//
//        //设置列的宽度
//        columns[0].setPrefWidth(120);
//        columns[1].setPrefWidth(80);
//        columns[2].setPrefWidth(120);
//        columns[3].setPrefWidth(120);
//        columns[4].setPrefWidth(80);
//
//
//        //设置CellFactory,定义每一列单元格的显示
//        columns[0].setCellFactory((param) -> {
//            return new GroupTableTreeCell("name");
//        });
//        columns[1].setCellFactory((param) -> {
//            return new GroupTableTreeCell("Id");
//        });
//        columns[2].setCellFactory((param) -> {
//            return new GroupTableTreeCell("profile");
//        });
//        columns[3].setCellFactory((param) -> {
//            return new GroupTableTreeCell("location");
//        });
//        columns[4].setCellFactory((param)->{
//            return new GroupTableTreeCell("size");
//        });
//    }
//
//    /**
//     * 族群表：
//     * 数据的显示。
//     * 根节点进行了隐藏
//     * */
//    public void initGroupTreeData() {
//        treeTableView.setRoot(trainerRoot);
//        treeTableView.setShowRoot(false);
//
//        List<DragonTrainer> dragonTrainerList = new DragonTrainerDAOImpl().getList();
//        if (dragonTrainerList != null) {
//            for (DragonTrainer dragonTrainer : dragonTrainerList) {
//                TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);
//                trainerTreeItemList.add(treeItem);
//                trainerRoot.getChildren().add(treeItem);
//            }
//        }
//    }
//
//    /**
//     * 族群表：
//     * 单元格的显示
//     * */
//    class GroupTableTreeCell extends TreeTableCell<DragonGroup,DragonGroup> {
//        String columnID;
//
//        public GroupTableTreeCell(String columnID) {
//            this.columnID = columnID;
//        }
//
//        @Override
//        protected void updateItem(DragonGroup item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (empty || null == item) {
//                setGraphic(null);
//                setText(null);
//            } else {
//                setGraphic(null);
//                if (columnID.equals("name")) {
//                    this.setText(item.getName());
//                } else if (columnID.equals("Id")) {
//                    this.setText(String.valueOf(item.getDragonTrainerId()));
//                } else if (columnID.equals("dragonGroupId")) {
//                    this.setText(String.valueOf(item.getDragonGroupId()));
//                } else if (columnID.equals("dragonGroupName")) {
//                    int dragonGroupId = item.getDragonGroupId();
//                    //获得族群名字
//                    String dragonGroupName = new DragonGroupDAOImpl().get(dragonGroupId).getName();
//                    this.setText(dragonGroupName);
//                }
//            }
//        }
//    }
}
