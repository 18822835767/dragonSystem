package view;

import entity.Dragon;
import entity.DragonMom;
import entity.Foreigner;
import entity.Ticket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.IDragonMomDAO;
import model.IForeignerDAO;
import model.ITicketDAO;
import model.database.impl.DragonMomDAOImpl;
import model.database.impl.ForeignerDAOImpl;
import model.database.impl.TicketDAOImpl;
import util.DAOFactory;
import widget.AlertTool;
import widget.DialogTool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
/**
 * 买票.
 * 买票流程:外邦人先选票，选了票后看钱够不够，够的话买票成功，不够的话，买票失败.
 * */
public class BuyTicket {

    static IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    static ITicketDAO iTicketDAO = DAOFactory.getTicketDAOInstance();

    private BuyTicket() {
    }

    /**
     * 买票调用的方法.
     *
     * @param foreigner 买票的外邦人的引用
     * @param ticket 若买票成功，则指向票
     *
     * */
    public static boolean buyTicketView(Foreigner foreigner, Ticket ticket) {
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
                if (newValue.equals(Ticket.TYPE1)) {
                    info.setText("价钱:" + Ticket.PRICE1 + "\n有效次数:" + Ticket.TIMES1);
                } else if (newValue.equals(Ticket.TYPE2)) {
                    info.setText("价钱:" + Ticket.PRICE2 + "\n有效次数:" + Ticket.TIMES2);
                } else if (newValue.equals(Ticket.TYPE3)) {
                    info.setText("价钱:" + Ticket.PRICE3 + "\n有效次数:" + Ticket.TIMES3);
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
            if (comboBox.getValue().equals(Ticket.TYPE1)) {
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

            } else if (comboBox.getValue().equals(Ticket.TYPE2)) {
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

            } else if (comboBox.getValue().equals(Ticket.TYPE3)) {
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
            }
            //购买成功后
            ticket = iTicketDAO.get(foreigner.getForeignerId());//对象实例化
            return true;
        }
        return false;
    }

}
