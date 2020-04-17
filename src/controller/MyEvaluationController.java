package controller;

import entity.Dragon;
import entity.DragonTrainer;
import entity.Evaluation;
import entity.Foreigner;
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
import util.table.DragonTrainerTable;
import util.table.EvaluationTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 外邦人“评价”页面的控制器.
 * */
public class MyEvaluationController implements Initializable {
    @FXML
    private TreeTableView<Evaluation> treeTableView;

    private List<TreeItem<Evaluation>> treeItemList = new ArrayList<>();

    private TreeItem<Evaluation> root = new TreeItem<Evaluation>(new Evaluation());

    private IEvaluationDAO iEvaluationDAO = DAOFactory.getEvaluationDAOInstance();

    private Foreigner foreigner = null;//查看"评价"的外邦人的实例

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTreeTable();
        initTreeData();
    }

    public void changeMyEvaluation(ActionEvent actionEvent) {

    }

    public void deleteMyEvaluation(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("删除评价",
                "请输入评价的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int evaluationId = 0;
            try {
                evaluationId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            int items = iEvaluationDAO.delete(foreigner.getForeignerId(),evaluationId);

            if (items == 0) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "查找不到您的评论信息");
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
        String[] columnName = {"评价Id", "举办族群", "活动Id", "活动名字","评价内容"};
        double[] columnPrefWidth = {50, 80, 75, 100,150};
        String[] columnId = {"evaluationId", "groupName", "activityId", "activityName","content"};
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

    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }
}
