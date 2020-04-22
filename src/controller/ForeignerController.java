package controller;

import entity.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;
import util.Constants;
import util.DAOFactory;
import util.SwitchAccount;
import util.ViewManager;
import util.table.DragonGroupTable;
import util.table.DragonTable;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.TextInputDialogTool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 外邦人的控制器，实现Initializable接口来初始化.
 * 为了使代码简洁，查询方法使用了自定义的工具类AddNodeForPane。
 */
public class ForeignerController extends BaseController {
    @FXML
    private TreeTableView<Dragon> dragonTreeTableView;
    @FXML
    private TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button changeUser;

    private IDragonDAO iDragonDAO = DAOFactory.getDragonDAOInstance();

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    private ITicketDAO iTicketDAO = DAOFactory.getTicketDAOInstance();

    private IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    private IAccountDAO iAccountDAO = DAOFactory.getAccountDAOInstance();

    private TreeItem<Dragon> dragonRoot = new TreeItem<Dragon>(new Dragon());

    private TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());


    private Foreigner foreigner = null;

    private Ticket ticket = null;

    /**
     * 标志是否成功入园.
     */
    boolean enterSuccess = false;


    /**
     * 因为多列树控件中删除一行时，需要是原来加载进去的那个TreeItem对象，所以这里先把TreeItem存起来.
     * 为了表的显示而加载。
     */
    List<TreeItem<Dragon>> dragonTreeItemList = new ArrayList<>();

    List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 初始化.
     * 这里判断外邦人是否有信物，有信物且有效次数足够，入园。无信物或者有效次数不够，买信物。
     */

    public void init() {
        initDragonTreeTable();
        initDragonTreeData();
        initGroupTreeTable();
        initGroupTreeData();

        tabPaneListener();

        //初始化信物实例.
        ticket = iTicketDAO.get(foreigner.getForeignerId());

        //有信物的情况下
        if (ticket != null) {
            //如果信物的有效次数大于0，入园，有效次数-1
            if (ticket.getTimes() > 0) {
                dragonTreeTableView.setVisible(true);

                int leftTimes = ticket.getTimes() - 1;//票的剩余有效次数
                iTicketDAO.update(ticket.getTicketId(), leftTimes);//数据库中更新的有效次数
                ticket.setTimes(leftTimes);//更新对象中的值

                enterSuccess = true;
            } else {//有效次数不够，需要重新买票。
                enterSuccess = buyTicket();
            }
        } else {//从没买过票的情况。购买信物。
            enterSuccess = buyTicket();
        }

        //若买票成功，默认先显示“龙”的表
        if (enterSuccess) {
            dragonTreeTableView.setVisible(true);
        }


    }

    /**
     * TabPane监听器，用户点击不同的Tab则切换不同的表的信息
     */
    @FXML
    @Override
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if (newTab.getText().equals("龙") && enterSuccess) {
                    dragonTreeTableView.setVisible(true);
                    groupTreeTableView.setVisible(false);
                } else if (newTab.getText().equals("族群") && enterSuccess) {
                    dragonTreeTableView.setVisible(false);
                    groupTreeTableView.setVisible(true);
                }
            }
        });
    }

    /**
     * 切换用户.
     */
    @FXML
    @Override
    public void switchAccount(ActionEvent actionEvent) {
        SwitchAccount.changeUser(changeUser);
    }

    /**
     * 通过id来查询龙的信息.
     * 外邦人看不到属性:年龄。
     */
    @FXML
    public void queryDragon(ActionEvent actionEvent) {
        if (!enterSuccess) {
            return;
        }

        //如果成功入园
        Optional<String> result = TextInputDialogTool.showTextInput("查询龙的信息",
                "请输入龙的Id", "Id:");
        if (result.isPresent()) {
            int dragonId = 0;

            try {
                dragonId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            //找龙
            Dragon dragon = iDragonDAO.get(dragonId);

            //没找到的情况下
            if (dragon == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该龙的信息");
                return;
            }

            //找到的情况下
            VBox vBox = new VBox(10);

            Text t_id = new Text("龙的Id:" + dragon.getDragonId());
            Text t_name = new Text("名字:" + dragon.getName());
            Text t_sex = new Text("性别:" + dragon.getSex());
            Text t_profile = new Text("简介:" + dragon.getProfile());
            Text t_training = new Text("是否在训练:" + dragon.isTraining());
            Text t_healthy = new Text("是否健康:" + dragon.isHealthy());

            vBox.getChildren().addAll(t_id, t_name, t_sex, t_profile, t_training, t_healthy);

            DialogTool.showDialog("龙的信息", vBox, "确定", null).showAndWait();
        }
    }

    /**
     * 对族群的信息进行查询.
     * 外邦人看不到属性:地理位置。
     */
    @FXML
    public void queryDragonGroup(ActionEvent actionEvent) {
        if (!enterSuccess) {
            return;
        }

        //如果成功入园
        Optional<String> result = TextInputDialogTool.showTextInput("查询族群信息",
                "请输入族群的Id", "Id:");
        if (result.isPresent()) {
            int dragonGroupId = 0;
            try {
                dragonGroupId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            //找族群
            DragonGroup dragonGroup = iDragonGroupDAO.get(dragonGroupId);

            //没找到的情况下
            if (dragonGroup == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
                return;
            }

            //找到的情况下
            VBox vBox = new VBox(10);

            Text t_name = new Text("名字:" + dragonGroup.getName());
            Text t_id = new Text("Id:" + dragonGroup.getId());
            Text t_profile = new Text("简介:" + dragonGroup.getProfile());
            Text t_size = new Text("大小:" + dragonGroup.getSize());

            vBox.getChildren().addAll(t_name, t_id, t_profile, t_size);

            DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();


        }

    }

    /**
     * 族群表.
     * 设置列名、列宽
     * 调用工具类
     * 外邦人看不到族群的Id属性、地理位置
     */
    public void initGroupTreeTable() {
        String[] columnName = {"族群名字", "Id", "简介", "大小"};
        double[] columnPrefWidth = {120, 80, 120, 120, 80};
        String[] columnId = {DragonGroupTable.NAME, DragonGroupTable.ID, DragonGroupTable.PROFILE, DragonGroupTable.SIZE};
        DragonGroupTable.getInstance().initGroupTreeTable(groupTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 族群表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        DragonGroupTable.getInstance().initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

    /**
     * 龙表.
     * 设置列名、列宽
     * 调用工具类
     * 外邦人看不到龙的年龄属性。
     */
    public void initDragonTreeTable() {
        String[] columnName = {"名字", "Id", "性别", "简介", "训练", "健康"};
        double[] columnPrefWidth = {120, 80, 80, 120, 80, 80};
        String[] columnId = {DragonTable.NAME, DragonTable.ID, DragonTable.SEX, DragonTable.PROFILE,
                DragonTable.TRAINING, DragonTable.HEALTHY};
        DragonTable.getInstance().initDragonTreeTable(dragonTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 龙表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initDragonTreeData() {
        DragonTable.getInstance().initDragonTreeData(dragonTreeTableView, dragonRoot, dragonTreeItemList);
    }

    /**
     * 买票方法.
     */
    public boolean buyTicket() {
        ComboBox<String> comboBox = new ComboBox<>();
        Optional<ButtonType> result = buyTicketView(comboBox);

        //如果外邦人选择了买票。主要负责数据的更新
        if (result.isPresent() && comboBox.getValue() != null) {
            iTicketDAO.delete(foreigner.getForeignerId());//删除以前的票。以前没有票也不影响.
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentTime = timeFormat.format(date);//得出当前买票时间
            double moneyTub = iDragonMomDAO.get().getMoneyTub();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            //因为本次入园，所以购买成功后有效次数立即-1。
            switch (comboBox.getValue()) {
                case Constants.TicketConstant.TYPE1: {
                    //如果用户买了一等票
                    if (foreigner.getMoney() >= Constants.TicketConstant.PRICE1) {
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(), Constants.TicketConstant.PRICE1,
                                Constants.TicketConstant.TYPE1, currentTime, Constants.TicketConstant.TIMES1 - 1,
                                false);

                        double balance = foreigner.getMoney() -Constants.TicketConstant.PRICE1;//用户剩余的钱
                        foreigner.setMoney(balance);//更新对象中的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(moneyTub + Constants.TicketConstant.PRICE1);//更新数据库的金库

                        //保存账目
                        iAccountDAO.save(foreigner.getForeignerId(),Constants.TicketConstant.PRICE1,
                                format.format(new Date()), Constants.AccountConstant.PURCHASE);
                    } else {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "购买失败", null, "余额不足");
                        return false;
                    }
                    break;
                }
                case Constants.TicketConstant.TYPE2: {
                    //如果用户购买了二等票
                    if (foreigner.getMoney() >= Constants.TicketConstant.PRICE2) {
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(),Constants.TicketConstant.PRICE2,
                                Constants.TicketConstant.TYPE2, currentTime, Constants.TicketConstant.TIMES2 - 1,
                                false);

                        double balance = foreigner.getMoney() - Constants.TicketConstant.PRICE2;//外邦人剩余的钱
                        foreigner.setMoney(balance);//更新对象的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(moneyTub +Constants.TicketConstant.PRICE2);//更新数据库的金库

                        //保存账目
                        iAccountDAO.save(foreigner.getForeignerId(),Constants.TicketConstant.PRICE2,
                                format.format(new Date()), Constants.AccountConstant.PURCHASE);
                    } else {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "购买失败", null, "余额不足");
                        return false;
                    }
                    break;
                }
                case Constants.TicketConstant.TYPE3: {
                    //如果用户购买了三等票
                    if (foreigner.getMoney() >= Constants.TicketConstant.PRICE3) {
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(), Constants.TicketConstant.PRICE3,
                                Constants.TicketConstant.TYPE3,currentTime, Constants.TicketConstant.TIMES3 - 1,
                                false);

                        double balance = foreigner.getMoney() - Constants.TicketConstant.PRICE3;//外邦人剩余的钱
                        foreigner.setMoney(balance);//更新对象的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(moneyTub + Constants.TicketConstant.PRICE3);//更新数据库的金库

                        //保存账目
                        iAccountDAO.save(foreigner.getForeignerId(),Constants.TicketConstant.PRICE3,
                                format.format(new Date()), Constants.AccountConstant.PURCHASE);
                    } else {
                        AlertTool.showAlert(Alert.AlertType.WARNING, "购买失败", null, "余额不足");
                        return false;
                    }
                    break;
                }
                default:
                    break;
            }

            //购买成功后
            ticket = iTicketDAO.get(foreigner.getForeignerId());//对象实例化
            return true;
        }

        return false;
    }

    /**
     * 主要负责买票窗口的显示，显示的有票的类型，票价，不负责数据的更新与处理.
     *
     * @param comboBox 传入的下拉菜单
     * @return 返回用户的选择
     */
    public Optional<ButtonType> buyTicketView(ComboBox<String> comboBox) {
        //买票窗口
        AnchorPane anchorPane = new AnchorPane();

        comboBox.getItems().addAll(Constants.TicketConstant.TYPE1, Constants.TicketConstant.TYPE2,
                Constants.TicketConstant.TYPE3);//添加选项
        comboBox.setEditable(false);//不可编辑
        Text t_balance = new Text("您的余额:" + foreigner.getMoney());//外邦人的余额
        Text info = new Text("价钱:" + "\n有效次数:");//显示票的有关信息

        anchorPane.getChildren().addAll(t_balance, comboBox, info);

        AnchorPane.setTopAnchor(t_balance, 5.0);
        AnchorPane.setLeftAnchor(t_balance, 5.0);
        AnchorPane.setTopAnchor(info, 20.0);
        AnchorPane.setLeftAnchor(info, 150.0);
        AnchorPane.setRightAnchor(info, 20.0);
        AnchorPane.setTopAnchor(comboBox, 25.0);
        AnchorPane.setLeftAnchor(comboBox, 15.0);

        //监听器,对于票的信息进行展示(票价+有效次数)
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                switch (newValue) {
                    case Constants.TicketConstant.TYPE1:
                        info.setText("价钱:" + Constants.TicketConstant.PRICE1 + "\n有效次数:" + Constants.TicketConstant.TIMES1);
                        break;
                    case Constants.TicketConstant.TYPE2:
                        info.setText("价钱:" + Constants.TicketConstant.PRICE2 + "\n有效次数:" + Constants.TicketConstant.TIMES2);
                        break;
                    case Constants.TicketConstant.TYPE3:
                        info.setText("价钱:" + Constants.TicketConstant.PRICE3 + "\n有效次数:" + Constants.TicketConstant.TIMES3);
                        break;
                    default:
                        break;

                }
            }
        });
        //弹出买票信息的窗口
        return DialogTool.showDialog("请买票", anchorPane, "确定",
                null).showAndWait();
    }


    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }

    /**
     * 点击事件。弹出弹窗显示外邦人的信息.
     */
    @FXML
    public void showMyInfo(ActionEvent actionEvent) {
        VBox vBox = new VBox(15);

        Text t_name = new Text("姓名:  " + foreigner.getName());
        t_name.setFont(new Font(15));
        Text t_money = new Text("金币:  " + foreigner.getMoney());
        t_money.setFont(new Font(15));
        //判断票是否为空，为空则有效次数显示为0次
        Text t_ticketTimes = new Text("票的有效次数:  " + (ticket == null ? 0 : ticket.getTimes()));
        t_ticketTimes.setFont(new Font(15));

        vBox.getChildren().addAll(t_name, t_money, t_ticketTimes);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPadding(new Insets(15));

        DialogTool.showDialog("我的信息", vBox, "确定", null).showAndWait();
    }

    /**
     * 申请退票的点击事件
     */
    @FXML
    public void backTicket(ActionEvent actionEvent) {
        //可以申请退票的条件
        if ((ticket != null) && (ticket.getTimes() > 0)) {
            if (!ticket.isBacking()) {
                //有票且票的有效次数大于0
                VBox vBox = new VBox(10);

                Text t_times = new Text("剩余有效次数: " + ticket.getTimes());
                t_times.setFont(new Font(15));
                Text t_ask = new Text("您确定退票吗?");
                t_ask.setFont(new Font(15));

                vBox.getChildren().addAll(t_times, t_ask);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setPadding(new Insets(10));

                Optional<ButtonType> result = DialogTool.showDialog("退票", vBox, "确定",
                        "取消").showAndWait();

                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    iTicketDAO.update(ticket.getTicketId(), true);//更新数据库的退票状态
                    ticket.setBacking(true);//更新对象的值
                    AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "已申请退票");
                }
            } else {
                //之前已经申请过退票
                AlertTool.showAlert(Alert.AlertType.INFORMATION, null, null, "退票处理中，请耐心等待");
            }

        }

        //无法申请退票
        if (ticket == null || ticket.getTimes() == 0) {
            //没票或者票的有效次数不够
            AlertTool.showAlert(Alert.AlertType.ERROR, null, null, "您尚未购票或者票的有效次数不足");
        }

    }

    /**
     * 查看活动.
     */
    @FXML
    public void showActivity(ActionEvent actionEvent) {
        if (enterSuccess) {
            FXMLLoader fx = null;
            try {
                fx = ViewManager.openView(ViewManager.MY_ACTIVITY_URL, "活动信息", 600.0, 400.0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fx != null) {
                //得到控制器
                MyActivityController activityController = (MyActivityController) fx.getController();
                //传入外邦人实例
                activityController.setForeigner(foreigner);
            }
        }
    }

    /**
     * 展示我的评价.
     */
    @FXML
    public void showMyEvaluation(ActionEvent actionEvent) {
        try {
            FXMLLoader fx = ViewManager.openView(ViewManager.MY_EVALUATION_URL, "评价界面", 600.0,
                    400.0);

            //为控制器传入foreigner实例
            MyEvaluationController controller = (MyEvaluationController) fx.getController();
            controller.setForeigner(foreigner);
            controller.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示我的账目(信物的购买与退货).
     * */
    @FXML
    public void showMyAccount(ActionEvent actionEvent) {
        try {
            FXMLLoader fx = ViewManager.openView(ViewManager.MY_ACCOUNT_URL, "账目界面", 600.0,
                    400.0);

            //为控制器传入foreigner实例
            MyAccountController controller = (MyAccountController) fx.getController();
            controller.setForeigner(foreigner);
            controller.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
