package controller;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * 为了可以初始化，所以继承该接口
 * */
public class DragonMonController implements Initializable {
    @FXML
    TreeTableView<DragonTrainer> treeTableView;
    @FXML
    TabPane tabPane;

    TreeItem<DragonTrainer> root = new TreeItem<DragonTrainer>(new DragonTrainer());

    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来
     * */
    List<TreeItem<DragonTrainer>> treeItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTreeTable();//默认先显示驯龙高手的信息
        initTreeData();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     * */
    public void tabPaneListener(){
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                System.out.println(t1.getText());
            }
        });
    }

    /**
     * 添加驯龙高手信息
     * */
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
        vBox.getChildren().addAll(t_name,t_dragonGroupId,t_username,t_password);

        vBox.setSpacing(10);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(vBox);

        ButtonType cancel =new ButtonType("取消",ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().add(cancel);
        ButtonType ok = new ButtonType("确定",ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().add(ok);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("添加驯龙高手信息");

        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if(result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
            String name = t_name.getText();
            int dragonGroupId = Integer.valueOf(t_dragonGroupId.getText().trim());
            String username = t_username.getText().trim();
            String password = t_password.getText().trim();
            new DragonTrainerDAOImpl().save(dragonGroupId,name,username,password);//数据库保存数据

            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(username,password);
            TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);//试下TreeItem后面加<>会怎么样
            treeItemList.add(treeItem);
            root.getChildren().add(treeItem);
        }

    }

    /**
     * 删除驯龙高手信息
     * */
    public void deleteDragonTrainer(ActionEvent actionEvent) {
        VBox vBox = new VBox();

        TextField t_dragonTrainerId = new TextField();
        t_dragonTrainerId.setPromptText("驯龙高手Id");
        vBox.getChildren().add(t_dragonTrainerId);

        vBox.setAlignment(Pos.CENTER);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(vBox);

        ButtonType cancel =new ButtonType("取消",ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().add(cancel);
        ButtonType ok = new ButtonType("确定",ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().add(ok);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("删除驯龙高手信息");

        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if(result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
            int dragonTrainerId = Integer.valueOf(t_dragonTrainerId.getText().trim());
            DragonTrainer dragonTrainer = new DragonTrainerDAOImpl().get(dragonTrainerId);
            new DragonTrainerDAOImpl().delete(dragonTrainerId);
            /**
             * 从treeItemList找到驯龙高手相匹配的treeItem,然后从树控件中移除
             * */
            for(TreeItem<DragonTrainer> treeItem : treeItemList){
                if(treeItem.getValue().getDragonTrainerId() == dragonTrainerId){
                    root.getChildren().remove(treeItem);
                    break;
                }
            }
        }
    }

    /**
     * 查询驯龙高手信息
     * */
    public void queryDragonTrainer(ActionEvent actionEvent) {
    }

    /**
     * 修改驯龙高手信息
     * */
    public void changeDragonTrainer(ActionEvent actionEvent) {
    }

    //设置列
    public void initTreeTable(){
        //这里添加了多个列
        TreeTableColumn<DragonTrainer,DragonTrainer> columns [] = new TreeTableColumn[4];
        columns[0] = new TreeTableColumn("驯龙高手名字");
        columns[1] = new TreeTableColumn("Id");
        columns[2] = new TreeTableColumn("族群Id");
        columns[3] = new TreeTableColumn("族群名字");
        treeTableView.getColumns().addAll(columns);

        Callback cellValueFactory= new Callback() {
            @Override
            public Object call(Object o) {
                TreeTableColumn.CellDataFeatures p = (TreeTableColumn.CellDataFeatures) o;
                return p.getValue().valueProperty();
            }
        };
        for(int i=0;i<columns.length;i++){
            columns[i].setCellValueFactory(cellValueFactory);
        }

        //设置列的宽度
        columns[0].setPrefWidth(150);
        columns[1].setPrefWidth(80);
        columns[2].setPrefWidth(80);
        columns[3].setPrefWidth(120);

        //设置CellFactory,定义每一列单元格的显示
        columns[0].setCellFactory((param)->{
            return new MyTableTreeCell("name");
        });
        columns[1].setCellFactory((param)->{
            return new MyTableTreeCell("Id");
        });
        columns[2].setCellFactory((param)->{
            return new MyTableTreeCell("dragonGroupId");
        });
        columns[3].setCellFactory((param)->{
            return new MyTableTreeCell("dragonGroupName");
        });
    }

    //数据的初始化
    public void initTreeData(){
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        List<DragonTrainer> dragonTrainerList = new DragonTrainerDAOImpl().getList();
        if(dragonTrainerList != null){
            for(DragonTrainer dragonTrainer : dragonTrainerList){
                TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);
                treeItemList.add(treeItem);
                root.getChildren().add(treeItem);
            }
        }
    }

    //单元格的显示
    class MyTableTreeCell extends TreeTableCell<DragonTrainer,DragonTrainer>{
        String columnID;

        public MyTableTreeCell(String columnID){
            this.columnID = columnID;
        }

        @Override
        protected void updateItem(DragonTrainer item, boolean empty) {
            super.updateItem(item, empty);

            if(empty || null == item){
                setGraphic(null);
                setText(null);
            }else{
                setGraphic(null);
                if(columnID.equals("name")){
                    this.setText(item.getName());
                }else if(columnID.equals("Id")){
                    this.setText(String.valueOf(item.getDragonTrainerId()));
                }else if(columnID.equals("dragonGroupId")){
                    this.setText(String.valueOf(item.getDragonGroupId()));
                }else if(columnID.equals("dragonGroupName")){
                    int dragonGroupId = item.getDragonGroupId();
                    //获得族群名字
                    String dragonGroupName = new DragonGroupDAOImpl().get(dragonGroupId).getName();
                    this.setText(dragonGroupName);
                }
            }
        }
    }
}
