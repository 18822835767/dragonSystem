package util.table;

import entity.Evaluation;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IEvaluationDAO;
import util.DAOFactory;

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

    /**
     * 评价表：
     * 设置列名、列宽
     */
    public void initEvaluationTable(TreeTableView<Evaluation> evaluationTreeTableView, String[] columnName,
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
     */
    public void initEvaluationTreeData(TreeTableView<Evaluation> evaluationTreeTableView, TreeItem<Evaluation> evaluationRoot,
                                     List<TreeItem<Evaluation>> evaluationTreeItemList) {
        evaluationTreeTableView.setRoot(evaluationRoot);
        evaluationTreeTableView.setShowRoot(false);

        flushEvaluation(evaluationTreeItemList, evaluationRoot);
    }

    /**
     * 评价表：
     * 单元格内容的显示
     */
    class EvaluationTableTreeCell extends TreeTableCell<Evaluation, Evaluation> {
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

                    default:
                        break;
                }

            }
        }
    }

    /**
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
}
