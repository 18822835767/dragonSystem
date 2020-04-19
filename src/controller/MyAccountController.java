package controller;

import entity.Account;
import entity.Foreigner;
import entity.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

import java.util.List;

/**
 * “外邦人”显示账目时对应的控制器.
 * */
public class MyAccountController {
    @FXML
    private ListView<Account> listView;

    private static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private static IAccountDAO iAccountDAO = DAOFactory.getAccountDAOInstance();

    private ObservableList<Account> listData = FXCollections.observableArrayList();//数据源

    /**
     * 记录查看"账目"的是哪个外邦人.
     * */
    private Foreigner foreigner = null;

    public void init(){
        List<Account> accounts = iAccountDAO.getListById(foreigner.getForeignerId());

        //为listView设置数据源
        listView.setItems(listData);

        listData.addAll(accounts);

        //设置单元格生成器，负责每个单元格的显示
        listView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> accountListView) {
                return new MyAccountController.MyListCell();
            }
        });

    }

    /**
     * 负责单元格的显示.
     * */
    static class  MyListCell extends ListCell<Account>{
        @Override
        public void updateItem(Account item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null) {
                this.setGraphic(null);
            } else {
                this.setText("账目Id: "+item.getAccountId()+"   外邦人姓名: "+iForeignerDAO.get(item.getForeignerId()).getName()
                    +"   状态: "+item.getStatus()+"   金额: "+item.getMoney()+"   生成时间:"+item.getCreateTime());
            }
        }
    }

    public void setForeigner(Foreigner foreigner) {
        this.foreigner = foreigner;
    }
}
