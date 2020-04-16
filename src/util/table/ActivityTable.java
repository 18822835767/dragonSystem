package util.table;

import entity.Activity;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IActivityDAO;
import model.IDragonGroupDAO;
import util.DAOFactory;

import java.util.List;

public class ActivityTable {
    private volatile static ActivityTable instance = null;

    private ActivityTable(){}

    public static ActivityTable getInstance(){
        if(instance == null){
            synchronized (ActivityTable.class){
                if(instance == null){
                    instance = new ActivityTable();
                }
            }
        }
        return instance;
    }

    private IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    /**
     * 族群表：
     * 设置列名、列宽
     */
    public void initActivityTable(TreeTableView<Activity> activityTreeTableView, String[] columnName,
                                  double[] columnPrefWidth, String[] columnId) {
        //列的数量
        int columnNum = columnName.length;

        Callback cellValueFactory = new Callback() {
            @Override
            public Object call(Object o) {
                TreeTableColumn.CellDataFeatures p = (TreeTableColumn.CellDataFeatures) o;
                return p.getValue().valueProperty();
            }
        };

        for (int i = 0; i < columnNum; i++) {
            int finalI = i;
            TreeTableColumn<Activity,Activity> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            activityTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new ActivityTable.ActivityTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     */
    public void initActivityTreeData(TreeTableView<Activity> activityTreeTableView, TreeItem<Activity> activityRoot,
                                  List<TreeItem<Activity>> activityTreeItemList) {
        activityTreeTableView.setRoot(activityRoot);
        activityTreeTableView.setShowRoot(false);

        flushActivity(activityTreeItemList, activityRoot);
    }

    /**
     * 族群表：
     * 单元格内容的显示
     */
    class ActivityTableTreeCell extends TreeTableCell<Activity, Activity> {
        String columnID;

        public ActivityTableTreeCell(String columnID) {
            this.columnID = columnID;
        }


        @Override
        protected void updateItem(Activity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);

                switch (columnID) {
                    case "groupName":
                        setText(iDragonGroupDAO.get(item.getDragonGroupId()).getName());
                        break;
                    case "activityId":
                        setText(String.valueOf(item.getActivityId()));
                        break;
                    case "activityName":
                        setText(item.getName());
                        break;
                    case "startTime":
                        setText(item.getStartTime());
                        break;
                    case "overTime":
                        setText(String.valueOf(item.getOverTime()));
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
    public void flushActivity(List<TreeItem<Activity>> activityTreeItemList, TreeItem<Activity> activityRoot) {
        activityTreeItemList.clear();
        activityRoot.getChildren().clear();
        List<Activity> activityList = iActivityDAO.getList();
        if (activityList != null) {
            for (Activity activity : activityList) {
                TreeItem<Activity> treeItem = new TreeItem<>(activity);
                activityTreeItemList.add(treeItem);
                activityRoot.getChildren().add(treeItem);
            }
        }
    }
}
