package controller;

import entity.Activity;
import entity.DragonGroup;
import entity.Foreigner;
import entity.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import model.IActivityDAO;
import model.IEvaluationDAO;
import util.DAOFactory;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.SingleValueTool;
import util.control.TextInputDialogTool;
import util.table.ActivityTable;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;

/**
 * 外邦人打开活动界面时对应的控制器.
 * */
public class MyActivityController implements Initializable {
    @FXML
    private TreeTableView<Activity> treeTableView;

    private IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

    private IEvaluationDAO iEvaluationDAO = DAOFactory.getEvaluationDAOInstance();

    private TreeItem<Activity> activityRoot = new TreeItem<Activity>(new Activity());

    private List<TreeItem<Activity>> activityTreeItemList = new ArrayList<>();

    /**
     * 表明是哪一个外邦人.
     */
    private Foreigner foreigner = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initActivityTreeTable();
        initActivityTreeData();
    }

    /**
     * "观看活动"->外邦人.
     */
    @FXML
    public void viewActivity(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("观看活动", "请输入活动Id", "Id:");
        if (result.isPresent()) {
            int activityId = 0;
            try {
                activityId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            //外邦人只可以找到有效期范围内的活动
            Activity activity = iActivityDAO.getByTimeAndId(Integer.parseInt(result.get().trim()),LocalDate.now());

            if (activity == null) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "目前无该活动");
                return;
            }

            TextArea t_content = new TextArea();//展示活动内容
            t_content.setEditable(false);
            t_content.setText("活动内容: " + activity.getContent());
            t_content.setWrapText(true);
            t_content.setPrefColumnCount(25);//设置框的大小

            Optional<ButtonType> choice = DialogTool.showDialog("活动内容", t_content, "去评价",
                    "溜了溜了").showAndWait();
            //用户点击了"去评价"
            if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                remark(activityId);
            }
        }
    }

    /**
     * 外邦人"评价"时要调用的方法.
     **/
    public void remark(int activityId) {

        //如果用户已有该活动的评价记录，就不可以重复评价.
        if (iEvaluationDAO.getByActivityId(foreigner.getForeignerId(), activityId) != null) {
            AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "您已经评价过了");
            return;
        }

        VBox vBox = new VBox(10);

        HBox hBox = new HBox(5);
        Label l_hint = new Label("等级评价: ");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("5", "4", "3", "2", "1");//添加选项
        comboBox.setValue("5");//默认值
        hBox.getChildren().addAll(l_hint, comboBox);

        TextArea t_evaluation = new TextArea();//用户的评论内容
        t_evaluation.setPromptText("说点什么吧");
        t_evaluation.setWrapText(true);
        t_evaluation.setPrefColumnCount(25);//设置框的大小

        vBox.getChildren().addAll(hBox, t_evaluation);

        Optional<ButtonType> decision = DialogTool.showDialog("评价", vBox, "确定",
                "取消").showAndWait();

        //用户提交评价时
        if (decision.isPresent() && decision.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {

            if (t_evaluation.getText().trim().equals("")) {
                AlertTool.showAlert(Alert.AlertType.INFORMATION, "提示", "评价失败", "评价内容不能为空噢");
                return;
            }

            //如果用户在评价页面点击了"确定"
            int rank = Integer.parseInt(comboBox.getValue());//评价等级
            String content = t_evaluation.getText();//获取评价内容
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            int items = iEvaluationDAO.save(activityId, foreigner.getForeignerId(), rank, content,
                    dateFormat.format(new Date()));

            AlertTool.showAlert(Alert.AlertType.INFORMATION, "提示", null, "评价成功");
        }
    }



    /**
     * 活动表.
     * 设置列名、列宽
     * 调用工具类
     */
    public void initActivityTreeTable() {
        String[] columnName = {"活动Id", "活动名字", "举办的族群", "开始时间", "结束时间"};
        double[] columnPrefWidth = {80, 120, 120, 100, 100};
        String[] columnId = {ActivityTable.ACTIVITY_ID,ActivityTable.ACTIVITY_NAME,ActivityTable.GROUP_NAME,
                ActivityTable.START_TIME, ActivityTable.OVER_TIME};
        ActivityTable.getInstance().initActivityTable(treeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 活动表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initActivityTreeData() {
        ActivityTable.getInstance().initValidActivityTreeData(treeTableView, activityRoot, activityTreeItemList,LocalDate.now());
    }

    /**
     * 传入外邦人的实例.
     */
    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }


}
