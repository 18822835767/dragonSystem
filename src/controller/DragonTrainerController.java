package controller;

import entity.Dragon;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.InitDragonGroupView;
import view.InitDragonView;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DragonTrainerController{
    @FXML
    TreeTableView<Dragon> dragonTreeTableView;
    @FXML
    TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    TabPane tabPane;
    @FXML
    Button changeUser;
    @FXML
    Text text;

    private int dragonGroupId;


    TreeItem<Dragon> dragonRoot = new TreeItem<Dragon>(new Dragon());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());
    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来
     */
    List<TreeItem<Dragon>> dragonTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 初始化这里拿到族群的Id
     * */
    public void Init() {
        initDragonTreeTable();
        initDragonTreeData();
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

    public void changeUser(ActionEvent actionEvent) {
        try {
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(fx.getClassLoader().getResource("view/login.fxml"));
            GridPane gridPane = (GridPane) fx.load();
            Scene scene = new Scene(gridPane);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setHeight(280);
            newStage.setWidth(420);
            newStage.show();
            Stage oldStage = (Stage) changeUser.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDragon(ActionEvent actionEvent) {
    }

    public void deleteDragon(ActionEvent actionEvent) {
    }

    public void queryDragon(ActionEvent actionEvent) {
    }

    public void changeDragon(ActionEvent actionEvent) {
    }

    public void queryMyDragonGroup(ActionEvent actionEvent) {
    }

    public void changeMyDragonGroup(ActionEvent actionEvent) {
    }

    public void queryDragonGroup(ActionEvent actionEvent) {
    }

    public void changeDragonGroup(ActionEvent actionEvent) {
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

    public void initDragonTreeTable(){
        String [] columnName = {"Id","名字","性别","年龄","简介","训练","健康"};
        double [] columnPrefWidth = {80,120,80,80,120,80,80};
        String [] columnId = {"Id","name","sex","age","profile","training","healthy"};
        InitDragonView.initDragonTreeTable(dragonTreeTableView, columnName, columnPrefWidth, columnId);
    }

    public void initDragonTreeData(){
        InitDragonView.initDragonTreeData(dragonTreeTableView, dragonRoot, dragonTreeItemList, dragonGroupId);
    }

    public void setDragonGroupId(int dragonGroupId){
        this.dragonGroupId = dragonGroupId;
    }

}
