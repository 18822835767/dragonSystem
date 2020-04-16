package util.table;

import entity.DragonGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IDragonGroupDAO;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import util.DAOFactory;

import java.util.List;

/**
 * 负责族群列表TreeTableView的初始化(列名、列宽、数据)，封转成一个工具类.
 * 可以根据需要显示对应的列
 */
public class DragonGroupTable {
    private volatile static DragonGroupTable instance = null;

    private DragonGroupTable(){}

    public static DragonGroupTable getInstance(){
        if(instance == null){
            synchronized (DragonGroupTable.class){
                if(instance == null){
                    instance = new DragonGroupTable();
                }
            }
        }
        return instance;
    }

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    /**
     * 族群表：
     * 设置列名、列宽
     */
    public void initGroupTreeTable(TreeTableView<DragonGroup> groupTreeTableView, String[] columnName,
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
            TreeTableColumn<DragonGroup,DragonGroup> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            groupTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new DragonGroupTable.GroupTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     */
    public void initGroupTreeData(TreeTableView<DragonGroup> groupTreeTableView, TreeItem<DragonGroup> groupRoot,
                                         List<TreeItem<DragonGroup>> groupTreeItemList) {
        groupTreeTableView.setRoot(groupRoot);
        groupTreeTableView.setShowRoot(false);

        flushGroup(groupTreeItemList, groupRoot);
    }

    /**
     * 族群表：
     * 单元格内容的显示
     */
    class GroupTableTreeCell extends TreeTableCell<DragonGroup, DragonGroup> {
        String columnID;

        public GroupTableTreeCell(String columnID) {
            this.columnID = columnID;
        }


        @Override
        protected void updateItem(DragonGroup item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);

                switch (columnID) {
                    case "name":
                        setText(item.getName());
                        break;
                    case "Id":
                        setText(String.valueOf(item.getId()));
                        break;
                    case "profile":
                        setText(item.getProfile());
                        break;
                    case "location":
                        setText(item.getLocation());
                        break;
                    case "size":
                        setText(String.valueOf(item.getSize()));
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 刷新groupTreeItemList(储存treeItem的)和groupRoot
     */
    public void flushGroup(List<TreeItem<DragonGroup>> groupTreeItemList, TreeItem<DragonGroup> groupRoot) {
        groupTreeItemList.clear();
        groupRoot.getChildren().clear();
        List<DragonGroup> dragonGroupList = iDragonGroupDAO.getList();
        if (dragonGroupList != null) {
            for (DragonGroup dragonGroup : dragonGroupList) {
                TreeItem<DragonGroup> treeItem = new TreeItem<>(dragonGroup);
                groupTreeItemList.add(treeItem);
                groupRoot.getChildren().add(treeItem);
            }
        }
    }
}
