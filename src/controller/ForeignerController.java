package controller;

import entity.Dragon;
import entity.DragonGroup;
import entity.Foreigner;
import entity.Ticket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.*;
import util.AddNodeForPane;
import util.DAOFactory;
import view.ChangeUser;
import view.InitDragonGroupView;
import view.InitDragonView;
import widget.AlertTool;
import widget.DialogTool;
import widget.TextInputDialogTool;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 外邦人的控制器，实现Initializable接口来初始化.
 * 为了使代码简洁，查询方法使用了自定义的工具类AddNodeForPane。
 */
public class ForeignerController {
    @FXML
    TreeTableView<Dragon> dragonTreeTableView;
    @FXML
    TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    TabPane tabPane;
    @FXML
    Button changeUser;

    IDragonDAO iDragonDAO = DAOFactory.getDragonDAOInstance();

    IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    ITicketDAO iTicketDAO = DAOFactory.getTicketDAOInstance();

    IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    TreeItem<Dragon> dragonRoot = new TreeItem<Dragon>(new Dragon());

    TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());

    Foreigner foreigner = null;

    Ticket ticket = null;

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

        //初始化信物
        Ticket ticket = iTicketDAO.get(foreigner.getForeignerId());

        //有信物的情况下
        if (ticket != null) {
            //如果信物的有效次数大于0，入园，有效次数-1
            if (ticket.getTimes() > 0) {
                dragonTreeTableView.setVisible(true);
                int nowTimes = ticket.getTimes() - 1;//票的目前有效次数
                iTicketDAO.update(ticket.getTicketId(), nowTimes);//数据库中更新的有效次数
                enterSuccess = true;
            } else {//有效次数不够，需要重新买票。
                enterSuccess = buyTicketView(foreigner, ticket);
            }
        } else {//从没买过票的情况。购买信物。
            enterSuccess = buyTicketView(foreigner, ticket);
        }

        //若买票成功，默认先显示“龙”的表
        if(enterSuccess){
            dragonTreeTableView.setVisible(true);
        }


    }

    /**
     * TabPane监听器，用户点击不同的Tab则切换不同的表的信息
     */
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
    public void changeUser(ActionEvent actionEvent) {
        ChangeUser.changeUser(changeUser);
    }

    /**
     * 通过id来查询龙的信息.
     * 外邦人看不到属性:年龄。
     */
    public void queryDragon(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询龙的信息",
                "请输入龙的Id", "Id:");
        if (result.isPresent()) {
            int dragonId = Integer.parseInt(result.get());
            Dragon dragon = iDragonDAO.get(dragonId);
            if (dragon != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"龙的Id:" + dragon.getDragonId(), "名字:" + dragon.getName(),
                        "性别:" + dragon.getSex(), "简介:" + dragon.getProfile(), "是否在训练:" + dragon.isTraining(),
                        "是否健康:" + dragon.isHealthy()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("龙的信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该龙的信息");
            }
        }
    }

    /**
     * 对族群的信息进行查询.
     * 外邦人看不到属性:地理位置。
     */
    public void queryDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.textInputDialog("查询族群信息",
                "请输入族群的Id", "Id:");
        if (result.isPresent()) {
            int dragonGroupId = Integer.parseInt(result.get());
            DragonGroup dragonGroup = iDragonGroupDAO.get(dragonGroupId);
            if (dragonGroup != null) {
                VBox vBox = new VBox(10);

                String[] textContents = {"名字:" + dragonGroup.getName(), "Id:" + dragonGroup.getId(),
                        "简介:" + dragonGroup.getProfile(), "大小:" + dragonGroup.getSize()};
                AddNodeForPane.addTextForPane(vBox, textContents);

                DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
            } else {
                //自定义控件
                AlertTool.alert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
            }
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
        String[] columnId = {"name", "Id", "profile", "size"};
        InitDragonGroupView.initGroupTreeTable(groupTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 族群表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        InitDragonGroupView.initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
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
        String[] columnId = {"name", "Id", "sex", "profile", "training", "healthy"};
        InitDragonView.initDragonTreeTable(dragonTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 龙表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initDragonTreeData() {
        InitDragonView.initDragonTreeData(dragonTreeTableView, dragonRoot, dragonTreeItemList);
    }

    /**
     * 买票函数.
     * */
    public boolean buyTicketView(Foreigner foreigner, Ticket ticket) {
        //买票窗口
        AnchorPane anchorPane = new AnchorPane();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(Ticket.TYPE1, Ticket.TYPE2, Ticket.TYPE3);//添加选项

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
            @Override//s是oldvalue,t1是newvalue
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                switch (newValue){
                    case Ticket.TYPE1:
                        info.setText("价钱:" + Ticket.PRICE1 + "\n有效次数:" + Ticket.TIMES1);
                        break;
                    case Ticket.TYPE2:
                        info.setText("价钱:" + Ticket.PRICE2 + "\n有效次数:" + Ticket.TIMES2);
                        break;
                    case Ticket.TYPE3:
                        info.setText("价钱:" + Ticket.PRICE3 + "\n有效次数:" + Ticket.TIMES3);
                        break;
                    default:
                        break;

                }
            }
        });

        //弹出买票信息的窗口
        Optional<ButtonType> result = DialogTool.showDialog("请买票", anchorPane, "确定",
                null).showAndWait();

        //如果外邦人选择了买票
        if (result.isPresent() && comboBox.getValue() != null) {
            iTicketDAO.delete(foreigner.getForeignerId());//删除以前的票。以前没有票也不影响.
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentTime = timeFormat.format(date);//得出当前买票时间

            //因为本次入园，所以购买成功后有效次数立即-1。
            switch (comboBox.getValue()){
                case Ticket.TYPE1:
                {
                    //如果用户买了一等票
                    if(foreigner.getMoney() >= Ticket.PRICE1){
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(), Ticket.PRICE1, Ticket.TYPE1, currentTime,
                                Ticket.TIMES1 - 1, false);

                        double balance = foreigner.getMoney() - Ticket.PRICE1;//用户剩余的钱
                        foreigner.setMoney(balance);//更新对象中的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(Ticket.PRICE1);//更新数据库的金库
                    }else{
                        AlertTool.alert(Alert.AlertType.WARNING,"购买失败",null,"余额不足");
                        return false;
                    }
                    break;
                }
                case Ticket.TYPE2:
                {
                    //如果用户购买了二等票
                    if(foreigner.getMoney() >= Ticket.PRICE2){
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(), Ticket.PRICE2, Ticket.TYPE2, currentTime,
                                Ticket.TIMES2 - 1, false);

                        double balance = foreigner.getMoney() - Ticket.PRICE2;//外邦人剩余的钱
                        foreigner.setMoney(balance);//更新对象的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(Ticket.PRICE2);//更新数据库的金库
                    }else{
                        AlertTool.alert(Alert.AlertType.WARNING,"购买失败",null,"余额不足");
                        return false;
                    }
                    break;
                }
                case Ticket.TYPE3:
                {
                    //如果用户购买了三等票
                    if(foreigner.getMoney() >= Ticket.PRICE3){
                        //如果用户余额足够
                        iTicketDAO.save(foreigner.getForeignerId(), Ticket.PRICE3, Ticket.TYPE3, currentTime,
                                Ticket.TIMES3 - 1, false);

                        double balance = foreigner.getMoney() - Ticket.PRICE3;//外邦人剩余的钱
                        foreigner.setMoney(balance);//更新对象的值
                        iForeignerDAO.update(foreigner.getForeignerId(), balance);//更新数据库中外邦人的钱
                        iDragonMomDAO.update(Ticket.PRICE3);//更新数据库的金库
                    }else{
                        AlertTool.alert(Alert.AlertType.WARNING,"购买失败",null,"余额不足");
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


    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }
}
