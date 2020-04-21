package util.table;

import entity.Evaluation;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.IActivityDAO;
import model.IDragonGroupDAO;
import model.IEvaluationDAO;
import model.IForeignerDAO;
import util.DAOFactory;
import util.control.DialogTool;

import java.util.List;

public class EvaluationTable {
    private volatile static EvaluationTable instance = null;

    private EvaluationTable(){}

    public static EvaluationTable getInstance(){
        if(instance == null){
            synchronized (EvaluationTable.class){
                if(instance == null){
                    instance = new EvaluationTable();
                }
            }
        }
        return instance;
    }

    private IEvaluationDAO iEvaluationDAO = DAOFactory.getEvaluationDAOInstance();

    private static IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    private static IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

    private static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    /**
     * 一系列的columnID常量.
     * */
    public static final String EVALUATION_ID = "evaluationId";
    public static final String FOREIGNER_NAME = "foreignerName";
    public static final String GROUP_NAME = "groupName";
    public static final String ACTIVITY_ID = "activityId";
    public static final String ACTIVITY_NAME =  "activityName";
    public static final String CONTENT = "content";
    public static final String RANK = "rank";

    /**
     * 评价表：
     * 设置列名、列宽
     */
    public void initTreeTable(TreeTableView<Evaluation> evaluationTreeTableView, String[] columnName,
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
            //设置列
            TreeTableColumn<Evaluation,Evaluation> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            evaluationTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new EvaluationTable.EvaluationTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 评价表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 初始化数据时，显示所有的评价，为"龙妈"提供.
     */
    public void initTreeData(TreeTableView<Evaluation> treeTableView, TreeItem<Evaluation> root,
                             List<TreeItem<Evaluation>> treeItemList) {
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        flushEvaluation(treeItemList, root);
    }

    /**
     * 评价表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 初始化数据时，仅显示外邦人的评价，为"外邦人"提供.
     */
    public void initTreeData(TreeTableView<Evaluation> treeTableView, TreeItem<Evaluation> root,
                             List<TreeItem<Evaluation>> treeItemList,int foreignerId) {
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        flushEvaluation(treeItemList, root,foreignerId);
    }

    /**
     * 评价表：
     * 单元格内容的显示
     */
    private static class EvaluationTableTreeCell extends TreeTableCell<Evaluation, Evaluation> {
        String columnID;

        public EvaluationTableTreeCell(String columnID) {
            this.columnID = columnID;
        }


        @Override
        protected void updateItem(Evaluation item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);

                switch (columnID) {
                    case EVALUATION_ID:
                        this.setText(String.valueOf(item.getEvaluationId()));
                        break;
                    case FOREIGNER_NAME:
                        this.setText(iForeignerDAO.get(item.getForeignerId()).getName());
                        break;
                    case GROUP_NAME:
                        //评价Id->活动Id->族群Id->族群名字
                        this.setText(iDragonGroupDAO.get(iActivityDAO.getById(item.getActivityId()).getDragonGroupId()).
                                getName());
                        break;
                    case ACTIVITY_ID:
                        this.setText(String.valueOf(item.getActivityId()));
                        break;
                    case  ACTIVITY_NAME:
                        this.setText(iActivityDAO.getById(item.getActivityId()).getName());
                        break;
                    case CONTENT:
                        //展示评价内容(一行)
                        TextArea content = new TextArea();
                        content.setText(item.getContent());
                        content.setEditable(false);
                        content.setPrefRowCount(1);

                        content.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getClickCount() == 2){
                                    //双击评价内容可以放大查看
                                    TextArea text = new TextArea();
                                    text.setText(item.getContent());
                                    text.setEditable(false);
                                    text.setPrefColumnCount(15);
                                    text.setWrapText(true);
                                    DialogTool.showDialog("评价内容",text,"确定",null).showAndWait();
                                }
                            }
                        });
                        this.setGraphic(content);
                        break;
                    case RANK:
                        this.setText(String.valueOf(item.getRank()));
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 该方法针对于查找的数据是"所有评价"，即龙妈.
     * 刷新groupTreeItemList(储存treeItem的)和groupRoot
     */
    public void flushEvaluation(List<TreeItem<Evaluation>> evaluationTreeItemList, TreeItem<Evaluation> evaluationRoot) {
        evaluationTreeItemList.clear();
        evaluationRoot.getChildren().clear();
        List<Evaluation> evaluationList = iEvaluationDAO.getList();
        if (evaluationList != null) {
            for (Evaluation evaluation : evaluationList) {
                TreeItem<Evaluation> treeItem = new TreeItem<>(evaluation);
                evaluationTreeItemList.add(treeItem);
                evaluationRoot.getChildren().add(treeItem);
            }
        }
    }

    /**
     * 重载，该方法针对的是查找"个人评价",即外邦人.
     * 刷新groupTreeItemList(储存treeItem的)和groupRoot
     */
    public void flushEvaluation(List<TreeItem<Evaluation>> evaluationTreeItemList, TreeItem<Evaluation> evaluationRoot,
                                int foreignerId) {
        evaluationTreeItemList.clear();
        evaluationRoot.getChildren().clear();
        List<Evaluation> evaluationList = iEvaluationDAO.getList(foreignerId);
        if (evaluationList != null) {
            for (Evaluation evaluation : evaluationList) {
                TreeItem<Evaluation> treeItem = new TreeItem<>(evaluation);
                evaluationTreeItemList.add(treeItem);
                evaluationRoot.getChildren().add(treeItem);
            }
        }
    }
}
