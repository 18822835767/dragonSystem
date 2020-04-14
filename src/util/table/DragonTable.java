package util.table;

import entity.Dragon;
import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IDragonDAO;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import util.DAOFactory;

import java.util.List;

/**
 * 负责族群中龙的列表TreeTableView的初始化(列名、列宽、数据)，封转成一个工具类.
 * 可以根据需要显示对应的列
 */
public class DragonTable {
    private static IDragonDAO iDragonDAO = DAOFactory.getDragonDAOInstance();

    /**
     * 龙的表.
     * 设置列名、列宽
     */
    public static void initDragonTreeTable(TreeTableView<Dragon> dragonTreeTableView, String[] columnName,
                                           double[] columnPrefWidth, String[] columnId) {
        //列的数量
        int columnNum = columnName.length;

        //这里的警告实在不会解决...
        Callback cellValueFactory = new Callback() {
            @Override
            public Object call(Object o) {
                TreeTableColumn.CellDataFeatures p = (TreeTableColumn.CellDataFeatures) o;
                return p.getValue().valueProperty();
            }
        };

        for (int i = 0; i < columnNum; i++) {
            int finalI = i;
            TreeTableColumn<Dragon,Dragon> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            dragonTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new DragonTable.DragonTableTreeCell(columnId[finalI]);
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

                switch (columnID) {
                    case "Id":
                        setText(String.valueOf(item.getDragonId()));
                        break;
                    case "name":
                        setText(item.getName());
                        break;
                    case "sex":
                        setText(String.valueOf(item.getSex()));
                        break;
                    case "age":
                        setText(String.valueOf(item.getAge()));
                        break;
                    case "profile":
                        setText(item.getProfile());
                        break;
                    case "training":
                        setText(String.valueOf(item.isTraining()));
                        break;
                    case "healthy":
                        setText(String.valueOf(item.isHealthy()));
                        break;
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
        List<Dragon> dragonList = iDragonDAO.getList(dragonGroupId);
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem<>(dragon);
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
        List<Dragon> dragonList = iDragonDAO.getList();
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem<>(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }
        }
    }
}
