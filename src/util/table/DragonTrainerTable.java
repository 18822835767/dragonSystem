package util.table;

import entity.DragonGroup;
import entity.DragonTrainer;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IDragonGroupDAO;
import model.IDragonTrainerDAO;
import model.database.impl.DragonDAOImpl;
import model.database.impl.DragonGroupDAOImpl;
import model.database.impl.DragonTrainerDAOImpl;
import util.DAOFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责驯龙高手列表TreeTableView的初始化(列名、列宽、数据)，封转成一个工具类.
 * 可以根据需要显示对应的列
 * */
public class DragonTrainerTable {
    private volatile static DragonTrainerTable instance = null;

    private DragonTrainerTable(){}

    public static DragonTrainerTable getInstance(){
        if(instance == null){
            synchronized (DragonTrainerTable.class){
                if(instance == null){
                    instance = new DragonTrainerTable();
                }
            }
        }
        return instance;
    }

    private IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    /**
     * 一系列columnID常量.
     * */
    public static final String NAME = "name";
    public static final String ID = "Id";
    public static final String DRAGON_GROUP_ID = "dragonGroupId";
    public static final String DRAGON_GROUP_NAME = "dragonGroupName";

    /**
     * 驯龙高手表：
     * 设置列名、列宽
     */
    public void initTrainerTreeTable(TreeTableView<DragonTrainer> trainerTreeTableView, String [] columnName,
                                     double [] columnPrefWidth, String [] columnId) {
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
            TreeTableColumn<DragonTrainer,DragonTrainer> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            trainerTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new DragonTrainerTable.TrainerTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     */
    public void initTrainerTreeData(TreeTableView<DragonTrainer> trainerTreeTableView, TreeItem<DragonTrainer> trainerRoot,
                                           List<TreeItem<DragonTrainer>> trainerTreeItemList) {
        trainerTreeTableView.setRoot(trainerRoot);
        trainerTreeTableView.setShowRoot(false);

        flushTrainer(trainerTreeItemList,trainerRoot);
    }

    /**
     * 驯龙高手表：
     * 单元格内容的显示
     */
    class TrainerTableTreeCell extends TreeTableCell<DragonTrainer, DragonTrainer> {
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

                switch (columnID){
                    case NAME:
                        this.setText(item.getName());
                        break;
                    case ID:
                        this.setText(String.valueOf(item.getDragonTrainerId()));
                        break;
                    case DRAGON_GROUP_ID:
                        this.setText(String.valueOf(item.getDragonGroupId()));
                        break;
                    case DRAGON_GROUP_NAME:
                        int dragonGroupId = item.getDragonGroupId();
                        //获得族群名字
                        String dragonGroupName = iDragonGroupDAO.get(dragonGroupId).getName();
                        this.setText(dragonGroupName);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot
     */
    public void flushTrainer(List<TreeItem<DragonTrainer>> trainerTreeItemList,
                                    TreeItem<DragonTrainer> trainerRoot) {
        trainerTreeItemList.clear();
        trainerRoot.getChildren().clear();
        List<DragonTrainer> dragonTrainerList = iDragonTrainerDAO.getList();
        if (dragonTrainerList != null) {
            for (DragonTrainer dragonTrainer : dragonTrainerList) {
                TreeItem<DragonTrainer> treeItem = new TreeItem<>(dragonTrainer);
                trainerTreeItemList.add(treeItem);
                trainerRoot.getChildren().add(treeItem);
            }
        }
    }
}
