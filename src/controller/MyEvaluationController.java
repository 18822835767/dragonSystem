package controller;

import entity.Dragon;
import entity.DragonTrainer;
import entity.Evaluation;
import entity.Foreigner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.IEvaluationDAO;
import util.CheckValid;
import util.DAOFactory;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.TextInputDialogTool;
import util.table.DragonTrainerTable;
import util.table.EvaluationTable;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 外邦人“评价”页面的控制器.
 * */
public class MyEvaluationController{
    @FXML
    private TreeTableView<Evaluation> treeTableView;

    private List<TreeItem<Evaluation>> treeItemList = new ArrayList<>();

    private TreeItem<Evaluation> root = new TreeItem<Evaluation>(new Evaluation());

    private IEvaluationDAO iEvaluationDAO = DAOFactory.getEvaluationDAOInstance();

    private Foreigner foreigner = null;//查看"评价"的外邦人的实例


    public void init() {
        initTreeTable();
        initTreeData();
    }

    /**
     * 外邦人修改评价.
     * */
    public void changeMyEvaluation(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput(null, "请输入评价的Id",
                "Id:");
        if (result.isPresent()) {
            int evaluationId = 0;
            try {
                evaluationId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            Evaluation evaluation = iEvaluationDAO.get(foreigner.getForeignerId(),evaluationId);

            if(evaluation != null){
                //童虎修改评价
                VBox vBox = new VBox(10);

                HBox hBox = new HBox(5);
                Label l_hint = new Label("等级评价: ");
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.getItems().addAll("5", "4", "3","2","1");//添加选项
                comboBox.setValue(String.valueOf(evaluation.getRank()));//默认值
                hBox.getChildren().addAll(l_hint,comboBox);

                TextArea content = new TextArea();//用户的评论内容
                content.setText(evaluation.getContent());
                content.setWrapText(true);
                content.setPrefColumnCount(25);//设置框的大小


                vBox.getChildren().addAll(hBox,content);

                Optional<ButtonType> decision = DialogTool.showDialog("修改评价",vBox,"确定",
                        "取消").showAndWait();

                if(decision.isPresent() && decision.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                    if(CheckValid.isEmpty(content.getText().trim())){
                        AlertTool.showAlert(Alert.AlertType.WARNING,null, "修改失败", "评价不能为空噢");
                        return;
                    }

                    //用户点击了确定后
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    iEvaluationDAO.update(evaluationId,Integer.parseInt(comboBox.getValue()),content.getText().trim(),
                            dateFormat.format(new Date()));

                    AlertTool.showAlert(Alert.AlertType.INFORMATION,null, "修改成功", null);
                }
            }else{
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查找不到该评价");
            }
        }
    }

    /**
     * 外邦人删除评价.
     * */
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
        String[] columnName = {"评价Id", "举办族群", "活动Id", "活动名字","评价内容","评价等级"};
        double[] columnPrefWidth = {50, 80, 75, 100,150,70};
        String[] columnId = {"evaluationId", "groupName", "activityId", "activityName","content","rank"};
        EvaluationTable.getInstance().initTreeTable(treeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * "评价"表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTreeData() {
        EvaluationTable.getInstance().initTreeData(treeTableView, root, treeItemList,foreigner.getForeignerId());
    }

    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }
}
