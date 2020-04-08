package controller;

import entity.Dragon;
import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DragonTrainerController implements Initializable {
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
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来
     */
    List<TreeItem<Dragon>> dragonTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     */
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if(newTab.getText().equals("驯龙高手")){

                }else if(newTab.getText().equals("族群")){

                }
            }
        });
    }

    public void changeUser(ActionEvent actionEvent) {
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
}
