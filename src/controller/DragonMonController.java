package controller;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
/**
 * 为了可以初始化，所以继承该接口
 * */
public class DragonMonController implements Initializable {
    @FXML
    TreeTableView<DragonTrainer> treeTableView;
    @FXML
    TabPane tabPane;

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
        TreeItem<DragonTrainer> root = new TreeItem<DragonTrainer>(new DragonTrainer());
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        List<DragonTrainer> dragonTrainerList = new DragonTrainerDAOImpl().getList();
        for(DragonTrainer dragonTrainer : dragonTrainerList){
            root.getChildren().add(new TreeItem(dragonTrainer));
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
