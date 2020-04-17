package controller;

import entity.Dragon;
import entity.DragonTrainer;
import entity.Evaluation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import util.table.DragonTrainerTable;
import util.table.EvaluationTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 外邦人“评价”页面的控制器.
 * */
public class MyEvaluationController implements Initializable {
    @FXML
    private TreeTableView<Evaluation> treeTableView;

    private List<TreeItem<Evaluation>> treeItemList = new ArrayList<>();

    private TreeItem<Evaluation> root = new TreeItem<Evaluation>(new Evaluation());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTreeTable();
        initTreeData();
    }

    public void changeMyEvaluation(ActionEvent actionEvent) {

    }

    public void deleteMyEvaluation(ActionEvent actionEvent) {

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


}
