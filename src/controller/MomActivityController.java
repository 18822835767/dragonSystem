package controller;

import entity.Activity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.IActivityDAO;
import model.IEvaluationDAO;
import util.CheckValid;
import util.DAOFactory;
import util.control.AlertTool;
import util.control.DialogTool;
import util.table.ActivityTable;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 龙妈打开活动界面时对应的控制器.
 * */
public class MomActivityController implements Initializable {
    @FXML
    private TreeTableView<Activity> treeTableView;

    private IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

    private TreeItem<Activity> activityRoot = new TreeItem<Activity>(new Activity());

    private List<TreeItem<Activity>> activityTreeItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initActivityTreeTable();
        initActivityTreeData();
    }

    /**
     * "添加活动"->龙妈.
     */
    public void addActivity(ActionEvent actionEvent) {
        //负责显示添加活动的界面
        VBox vBox = new VBox(10);

        TextField t_groupId = new TextField();
        TextField t_name = new TextField();
        TextArea t_content = new TextArea();
        DatePicker d_startTime = new DatePicker();
        DatePicker d_endTime = new DatePicker();

        t_groupId.setPromptText("族群Id");
        t_name.setPromptText("活动名字");
        t_content.setPromptText("活动内容");
        t_content.setPrefColumnCount(25);//设置TextArea输入框大小
        t_content.setWrapText(true);//自动换行
        d_startTime.setEditable(false);
        d_startTime.setPromptText("开始时间");
        d_endTime.setEditable(false);
        d_endTime.setPromptText("结束时间");

        //设置"活动结束时间"在"活动开始时间"之后，或者同一天
        d_endTime.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                DateCell cell = new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);

                        if(d_startTime.getValue() == null){
                            setDisable(true);
                        }else{
                            if(item.isBefore(d_startTime.getValue())){
                                setDisable(true);
                            }
                        }
                    }
                };
                return cell;
            }
        });

        vBox.getChildren().addAll(t_groupId, t_name, t_content, d_startTime, d_endTime);

        Optional<ButtonType> result = DialogTool.showDialog("添加活动", vBox, "确定", null).showAndWait();

        //负责数据的更新
        if (result.isPresent()) {
            int groupId = 0;
            try{
                groupId = Integer.parseInt(t_groupId.getText().trim());
            }catch (Exception e){
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "非法输入");
                return;
            }

            String name = t_name.getText().trim();
            String content = t_content.getText().trim();

            if(CheckValid.isEmpty(name,content) || d_startTime.getValue()==null || d_endTime.getValue() == null){
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "信息填写不完整");
                return;
            }

            String startTime = d_startTime.getValue().toString();
            String overTime = d_endTime.getValue().toString();

            int items = iActivityDAO.save(groupId, name, content, startTime, overTime);

            if (items == 0) {
                //说明没有成功插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING, null, "添加失败", "可能该族群并不存在");
            } else {
                //说明数据插入成功
                AlertTool.showAlert(Alert.AlertType.INFORMATION, null, "添加成功", null);
                //刷新下活动列表
                ActivityTable.getInstance().flushActivity(activityTreeItemList,activityRoot);
            }

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
                ActivityTable.START_TIME,ActivityTable.OVER_TIME};
        ActivityTable.getInstance().initActivityTable(treeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 活动表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initActivityTreeData() {
        ActivityTable.getInstance().initActivityTreeData(treeTableView, activityRoot, activityTreeItemList);
    }

}
