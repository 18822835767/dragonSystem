package util.table;

import entity.Evaluation;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.IActivityDAO;
import model.IDragonGroupDAO;
import model.IEvaluationDAO;
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

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    private IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

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
     */
    public void initTreeData(TreeTableView<Evaluation> evaluationTreeTableView, TreeItem<Evaluation> evaluationRoot,
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
                    case "evaluationId":
                        this.setText(String.valueOf(item.getEvaluationId()));
                        break;
//                    case "foreignerId":
//                        break;
//                    case "foreignerName":
//                        break;
                    case "groupName":
                        //评价Id->活动Id->族群Id->族群名字
                        this.setText(iDragonGroupDAO.get(iActivityDAO.getById(item.getActivityId()).getDragonGroupId()).
                                getName());
                        break;
                    case "activityId":
                        this.setText(String.valueOf(item.getActivityId()));
                        break;
                    case  "activityName":
                        this.setText(iActivityDAO.getById(item.getActivityId()).getName());
                        break;
                    case "content":
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
