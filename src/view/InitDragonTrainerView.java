package view;

import entity.DragonTrainer;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;

import java.util.List;

/**
 * 负责显示驯龙高手列表TreeTableView，封转成一个View的工具类.
 * 可以根据需要显示对应的列
 * */
public class InitDragonTrainerView {
    /**
     * 驯龙高手表：
     * 设置列名、列宽
     */
    public static void initTrainerTreeTable(TreeTableView<DragonTrainer> trainerTreeTableView, String [] columnName,
                                     double [] columnPrefWidth, String [] columnId) {
        //这里添加了多个列
        int columnNum = columnName.length;
        TreeTableColumn<DragonTrainer, DragonTrainer> columns[] = new TreeTableColumn[columnNum];
        for(int i=0;i<columnNum;i++){
            columns[i] = new TreeTableColumn(columnName[i]);
        }
        trainerTreeTableView.getColumns().addAll(columns);

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
        for(int i=0;i<columnNum;i++){
            columns[i].setPrefWidth(columnPrefWidth[i]);
        }


        //设置CellFactory,定义每一列单元格的显示
        for(int i=0;i<columnNum;i++){
            int finalI = i;
            columns[i].setCellFactory((param) -> {
                return new TrainerTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     */
    public static void initTrainerTreeData(TreeTableView<DragonTrainer> trainerTreeTableView, TreeItem<DragonTrainer> trainerRoot,
                                           List<TreeItem<DragonTrainer>> trainerTreeItemList) {
        trainerTreeTableView.setRoot(trainerRoot);
        trainerTreeTableView.setShowRoot(false);

        flushTrainer(trainerTreeItemList,trainerRoot);
    }

    /**
     * 驯龙高手表：
     * 单元格内容的显示
     */
    static class TrainerTableTreeCell extends TreeTableCell<DragonTrainer, DragonTrainer> {
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

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot
     */
    public static void flushTrainer(List<TreeItem<DragonTrainer>> trainerTreeItemList,
                                    TreeItem<DragonTrainer> trainerRoot) {
        trainerTreeItemList.clear();
        trainerRoot.getChildren().clear();
        List<DragonTrainer> dragonTrainerList = new DragonTrainerDAOImpl().getList();
        if (dragonTrainerList != null) {
            for (DragonTrainer dragonTrainer : dragonTrainerList) {
                TreeItem<DragonTrainer> treeItem = new TreeItem(dragonTrainer);
                trainerTreeItemList.add(treeItem);
                trainerRoot.getChildren().add(treeItem);
            }
        }
    }
}