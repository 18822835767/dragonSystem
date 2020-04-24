package controller;

import entity.Evaluation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import model.IEvaluationDAO;
import util.DAOFactory;
import util.control.AlertTool;
import util.control.TextInputDialogTool;
import util.table.EvaluationTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 龙妈打开"评价"页面时对应的控制器.
 * */
public class MomEvaluationController implements Initializable {
    @FXML
    private TreeTableView<Evaluation> treeTableView;

    private List<TreeItem<Evaluation>> treeItemList = new ArrayList<>();

    private TreeItem<Evaluation> root = new TreeItem<Evaluation>(new Evaluation());

    private IEvaluationDAO iEvaluationDAO = DAOFactory.getEvaluationDAOInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTreeTable();
        initTreeData();
    }

    /**
     * 龙妈删除评价.
     * */
    @FXML
    public void deleteEvaluation(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("删除评价",
                "请输入评价的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int evaluationId;
            try {
                evaluationId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            int items = iEvaluationDAO.delete(evaluationId);

            if (items == 0) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "查找不到该评论信息");
            } else {
                for (TreeItem<Evaluation> treeItem : treeItemList) {
                    if (treeItem.getValue().getEvaluationId() == evaluationId) {
                        treeItemList.remove(treeItem);
                        root.getChildren().remove(treeItem);
                        break;
                    }
                }
            }
        }
    }

    /**
     * "评价"表：
     * 设置列名、列宽
     * 调用工具类
     */
    public void initTreeTable() {
        String[] columnName = {"外邦人姓名","评价Id", "举办族群", "活动Id", "活动名字","评价内容","评价等级"};
        double[] columnPrefWidth = {100,50, 80, 75, 100,150,70};
        String[] columnId = {EvaluationTable.FOREIGNER_NAME,EvaluationTable.EVALUATION_ID, EvaluationTable.GROUP_NAME,
                EvaluationTable.ACTIVITY_ID,EvaluationTable.ACTIVITY_NAME,EvaluationTable.CONTENT,EvaluationTable.RANK};
        EvaluationTable.getInstance().initTreeTable(treeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * "评价"表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTreeData() {
        EvaluationTable.getInstance().initTreeData(treeTableView, root, treeItemList);
    }

}
