package controller;

import entity.Account;
import entity.Ticket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Callback;
import model.IAccountDAO;
import model.IDragonMomDAO;
import model.IForeignerDAO;
import model.ITicketDAO;
import util.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 处理退票的控制器.
 */
public class DealBackTicketsController implements Initializable {
    @FXML
    ListView<Ticket> listView;

    private ITicketDAO iTicketDAO = DAOFactory.getTicketDAOInstance();

    private static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    private IAccountDAO iAccountDAO = DAOFactory.getAccountDAOInstance();

    private ObservableList<Ticket> listData = FXCollections.observableArrayList();//数据源

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Ticket> tickets = iTicketDAO.getListByBacking(true);//得到所有的处于退票状态的Ticket

        //为listView设置数据源
        listView.setItems(listData);

        listData.addAll(tickets);

        //设置单元格生成器，负责每个单元格的显示
        listView.setCellFactory(new Callback<ListView<Ticket>, ListCell<Ticket>>() {
            @Override
            public ListCell<Ticket> call(ListView<Ticket> ticketListView) {
                return new MyListCell();
            }
        });
    }

    /**
     * 同意退票的按钮点击事件.
     */
    @FXML
    public void agree(ActionEvent actionEvent) {
        Iterator<Ticket> iterator = listData.iterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (ticket.isChecked()) {
                int foreignerId = ticket.getForeignerId();
                //要更新的钱
                double renewMoney = ticket.getTimes() * Ticket.Back_Price;
                //更新金库的钱
                iDragonMomDAO.update(iDragonMomDAO.get().getMoneyTub() - renewMoney);
                //更新外邦人的钱
                iForeignerDAO.update(foreignerId, iForeignerDAO.get(foreignerId).getMoney() + renewMoney);
                //将票的有效次数置0
                iTicketDAO.update(ticket.getTicketId(), 0);
                //更新票的状态
                iTicketDAO.update(ticket.getTicketId(), false);

                iterator.remove();

                iAccountDAO.save(foreignerId,renewMoney, LocalDate.now().toString(), Account.REFUND);
            }
        }
        listView.refresh();//刷新listView的显示
    }

    /**
     * 拒绝退票的点击事件.
     */
    @FXML
    public void refuse(ActionEvent actionEvent) {
        Iterator<Ticket> iterator = listData.iterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (ticket.isChecked()) {
                //更新票的状态
                iTicketDAO.update(ticket.getTicketId(), false);

                iterator.remove();
            }
        }
        listView.refresh();//刷新listView的显示
    }

    /**
     * 点击事件,同意所有的退票.
     */
    @FXML
    public void agreeAll(ActionEvent actionEvent) {
        for (Ticket ticket : listData) {
            ticket.setChecked(true);
        }
        agree(actionEvent);
    }

    /**
     * 点击事件，拒绝所有的退票.
     */
    @FXML
    public void refuseAll(ActionEvent actionEvent) {
        for (Ticket ticket : listData) {
            ticket.setChecked(true);
        }
        refuse(actionEvent);
    }


    //负责单元格的显示
    static class MyListCell extends ListCell<Ticket> {
        @Override
        public void updateItem(Ticket item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null) {
                this.setGraphic(null);
                this.setText(null);
            } else {
                CheckBox checkBox = new CheckBox();
                checkBox.setText("持票者:" + iForeignerDAO.get(item.getForeignerId()).getName() +
                        "   票的ID:" + item.getTicketId() + "   剩余有效次数:" + item.getTimes() + "   退款:" +
                        item.getTimes() * Ticket.Back_Price);
                checkBox.setFont(new Font(13));
                checkBox.setSelected(item.isChecked());//默认情况下是不选中状态
                this.setGraphic(checkBox);
                checkBox.selectedProperty().addListener(new MyCheckBoxListener(item));//给CheckBox设置监听器
            }
        }
    }

    //checkbox的监听器
    static class MyCheckBoxListener implements ChangeListener<Boolean> {
        Ticket ticket;

        public MyCheckBoxListener(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
            ticket.setChecked(newValue);
        }
    }
}
