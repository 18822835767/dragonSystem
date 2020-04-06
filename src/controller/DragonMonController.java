package controller;

import entity.DragonTrainer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.database.impl.DragonTrainerDAOImpl;

import java.util.List;

public class DragonMonController {
    @FXML
    TreeTableView<DragonTrainer> treeTableView;

    @FXML
    public void hhh(){//记得该方法删除掉啊
        initTreeTable();
        initTreeData();
    }

    //设置列
    public void initTreeTable(){
        //这里添加了多个列
        TreeTableColumn<DragonTrainer,DragonTrainer> columns [] = new TreeTableColumn[3];
        columns[0] = new TreeTableColumn("名字");
        columns[1] = new TreeTableColumn("Id");
        columns[2] = new TreeTableColumn("族群Id");
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

        //设置CellFactory,定义每一列单元格的显示
        columns[0].setCellFactory((param)->{
            return new MyTableTreeCell("name");
        });
        columns[1].setCellFactory((param)->{
            return new MyTableTreeCell("Id");
        });
        columns[2].setCellFactory((param)->{
            return new MyTableTreeCell("DragonGroupId");
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
                }else if(columnID.equals("DragonGroupId")){
                    this.setText(String.valueOf(item.getDragonGroupId()));
                }
            }
        }
    }
}
