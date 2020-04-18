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

import java.time.LocalDate;
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
     * 一系列columnID常量.
     * */
    public static final String GROUP_NAME = "groupName";
    public static final String ACTIVITY_ID = "activityId";
    public static final String ACTIVITY_NAME = "activityName";
    public static final String START_TIME = "startTime";
    public static final String OVER_TIME = "overTime";

    /**
     * 活动表：
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
     * 活动表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 仅显示所有的活动。
     */
    public void initActivityTreeData(TreeTableView<Activity> activityTreeTableView, TreeItem<Activity> activityRoot,
                                     List<TreeItem<Activity>> activityTreeItemList) {
        activityTreeTableView.setRoot(activityRoot);
        activityTreeTableView.setShowRoot(false);

        flushActivity(activityTreeItemList, activityRoot);
    }


    /**
     * 活动表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 仅显示传入的有效时间内的活动。
     */
    public void initValidActivityTreeData(TreeTableView<Activity> activityTreeTableView, TreeItem<Activity> activityRoot,
                                  List<TreeItem<Activity>> activityTreeItemList,LocalDate localDate) {
        activityTreeTableView.setRoot(activityRoot);
        activityTreeTableView.setShowRoot(false);

        flushValidActivity(activityTreeItemList, activityRoot,localDate);
    }

    /**
     * 活动表：
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
                    case GROUP_NAME:
                        setText(iDragonGroupDAO.get(item.getDragonGroupId()).getName());
                        break;
                    case ACTIVITY_ID:
                        setText(String.valueOf(item.getActivityId()));
                        break;
                    case ACTIVITY_NAME:
                        setText(item.getName());
                        break;
                    case START_TIME:
                        setText(item.getStartTime());
                        break;
                    case OVER_TIME:
                        setText(String.valueOf(item.getOverTime()));
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 刷新显示有效日期内的活动.
     */
    public void flushValidActivity(List<TreeItem<Activity>> activityTreeItemList, TreeItem<Activity> activityRoot,
                                   LocalDate localDate) {
        activityTreeItemList.clear();
        activityRoot.getChildren().clear();
        List<Activity> activityList = iActivityDAO.getValidList(localDate);
        if (activityList != null) {
            for (Activity activity : activityList) {
                TreeItem<Activity> treeItem = new TreeItem<>(activity);
                activityTreeItemList.add(treeItem);
                activityRoot.getChildren().add(treeItem);
            }
        }
    }

    /**
     * 刷新显示所有的的活动.
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
