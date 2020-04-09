package view;

import entity.Dragon;
import entity.DragonTrainer;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;

import java.util.List;

/**
 * 负责显示族群中龙的列表，封转成一个View的工具类.
 * 可以根据需要显示对应的列
 */
public class InitDragonView {
    /**
     * 龙的表.
     * 设置列名、列宽
     */
    public static void initDragonTreeTable(TreeTableView<Dragon> dragonTreeTableView, String[] columnName,
                                           double[] columnPrefWidth, String[] columnId) {
        //这里添加了多个列
        int columnNum = columnName.length;
        TreeTableColumn<Dragon, Dragon> columns[] = new TreeTableColumn[columnNum];
        for (int i = 0; i < columnNum; i++) {
            columns[i] = new TreeTableColumn(columnName[i]);
        }
        dragonTreeTableView.getColumns().addAll(columns);

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
        for (int i = 0; i < columnNum; i++) {
            columns[i].setPrefWidth(columnPrefWidth[i]);
        }


        //设置CellFactory,定义每一列单元格的显示
        for (int i = 0; i < columnNum; i++) {
            int finalI = i;
            columns[i].setCellFactory((param) -> {
                return new DragonTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 龙的表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 对某个族群的龙操作
     */
    public static void initDragonTreeData(TreeTableView<Dragon> dragonTreeTableView, TreeItem<Dragon> dragonRoot,
                                          List<TreeItem<Dragon>> dragonTreeItemList, int dragonGroupId) {
        dragonTreeTableView.setRoot(dragonRoot);
        dragonTreeTableView.setShowRoot(false);

        flushDragon(dragonTreeItemList, dragonRoot, dragonGroupId);
    }

    /**
     * 龙的表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 重载，对所有的龙操作。
     */
    public static void initDragonTreeData(TreeTableView<Dragon> dragonTreeTableView, TreeItem<Dragon> dragonRoot,
                                          List<TreeItem<Dragon>> dragonTreeItemList) {
        dragonTreeTableView.setRoot(dragonRoot);
        dragonTreeTableView.setShowRoot(false);

        flushDragon(dragonTreeItemList, dragonRoot);
    }

    /**
     * 龙的表.
     * 单元格内容的显示
     */
    static class DragonTableTreeCell extends TreeTableCell<Dragon, Dragon> {
        String columnID;

        public DragonTableTreeCell(String columnID) {
            this.columnID = columnID;
        }

        @Override
        protected void updateItem(Dragon item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);
                if (columnID.equals("Id")) {
                    setText(String.valueOf(item.getDragonId()));
                } else if (columnID.equals("name")) {
                    setText(item.getName());
                } else if (columnID.equals("sex")) {
                    setText(String.valueOf(item.getSex()));
                } else if (columnID.equals("age")) {
                    setText(String.valueOf(item.getAge()));
                } else if (columnID.equals("profile")) {
                    setText(item.getProfile());
                } else if (columnID.equals("training")) {
                    setText(String.valueOf(item.isTraining()));
                } else if (columnID.equals("healthy")) {
                    setText(String.valueOf(item.isHealthy()));
                }
            }
        }
    }

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot.
     * 对某个族群的龙操作。
     */
    public static void flushDragon(List<TreeItem<Dragon>> dragonTreeItemList,
                                   TreeItem<Dragon> dragonRoot, int dragonGroupId) {
        dragonTreeItemList.clear();
        dragonRoot.getChildren().clear();
        List<Dragon> dragonList = new DragonDAOImpl().getList(dragonGroupId);
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }
        }
    }

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot.
     * 对所有的龙操作。重载
     */
    public static void flushDragon(List<TreeItem<Dragon>> dragonTreeItemList, TreeItem<Dragon> dragonRoot) {
        dragonTreeItemList.clear();
        dragonRoot.getChildren().clear();
        List<Dragon> dragonList = new DragonDAOImpl().getList();
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }
        }
    }
}
