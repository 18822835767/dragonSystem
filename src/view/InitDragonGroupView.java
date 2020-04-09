package view;

import entity.DragonGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.database.impl.DragonGroupDAOImpl;

import java.util.List;

/**
 * 负责显示族群列表，封转成一个View的工具类.
 * 可以根据需要显示对应的列
 * */
public class InitDragonGroupView {
    /**
     * 族群表：
     * 设置列名、列宽
     */
    public static void initGroupTreeTable(TreeTableView<DragonGroup> groupTreeTableView,String [] columnName,
                                          double [] columnPrefWidth,String [] columnId) {
        //这里添加了多个列
        int columnNum = columnName.length;
        TreeTableColumn<DragonGroup, DragonGroup> columns[] = new TreeTableColumn[columnNum];
        for(int i=0;i<columnNum;i++){
            columns[i] = new TreeTableColumn(columnName[i]);
        }
        groupTreeTableView.getColumns().addAll(columns);

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
                return new InitDragonGroupView.GroupTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     */
    public static void initGroupTreeData(TreeTableView<DragonGroup> groupTreeTableView, TreeItem<DragonGroup> groupRoot,
                                         List<TreeItem<DragonGroup>> groupTreeItemList) {
        groupTreeTableView.setRoot(groupRoot);
        groupTreeTableView.setShowRoot(false);

        flushGroup(groupTreeItemList,groupRoot);
    }

    /**
     * 族群表：
     * 单元格内容的显示
     */
    static class GroupTableTreeCell extends TreeTableCell<DragonGroup, DragonGroup> {
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
                if (columnID.equals("name")) {
                    setText(item.getName());
                } else if (columnID.equals("Id")) {
                    setText(String.valueOf(item.getId()));
                } else if (columnID.equals("profile")) {
                    setText(item.getProfile());
                } else if (columnID.equals("location")) {
                    setText(item.getLocation());
                } else if (columnID.equals("size")) {
                    setText(String.valueOf(item.getSize()));
                }
            }
        }
    }

    /**
     * 刷新groupTreeItemList(储存treeItem的)和groupRoot
     */
    public static void flushGroup(List<TreeItem<DragonGroup>> groupTreeItemList, TreeItem<DragonGroup> groupRoot) {
        groupTreeItemList.clear();
        groupRoot.getChildren().clear();
        List<DragonGroup> dragonGroupList = new DragonGroupDAOImpl().getList();
        if (dragonGroupList != null) {
            for (DragonGroup dragonGroup : dragonGroupList) {
                TreeItem<DragonGroup> treeItem = new TreeItem(dragonGroup);
                groupTreeItemList.add(treeItem);
                groupRoot.getChildren().add(treeItem);
            }
        }
    }
}
