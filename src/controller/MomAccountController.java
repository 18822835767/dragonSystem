package controller;

import entity.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Callback;
import model.IAccountDAO;
import model.IForeignerDAO;
import util.DAOFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 龙妈打开账目对应的控制器.
 * */
public class MomAccountController implements Initializable {
    @FXML
    private ListView<Account> listView;

    private static IForeignerDAO iForeignerDAO = DAOFactory.getForeignerDAOInstance();

    private static IAccountDAO iAccountDAO = DAOFactory.getAccountDAOInstance();

    private ObservableList<Account> listData = FXCollections.observableArrayList();//数据源

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Account> accounts = iAccountDAO.getAllList();

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
                this.setText(null);
            } else {
                this.setText("账目Id: "+item.getAccountId()+"   外邦人姓名: "+iForeignerDAO.get(item.getForeignerId()).getName()
                        +"   状态: "+item.getStatus()+"   金额: "+item.getMoney()+"   生成时间:"+item.getCreateTime());
                this.setFont(new Font(13));
            }
        }
    }

    @FXML
    public void showAllAccount(ActionEvent actionEvent) {
        List<Account> accounts = iAccountDAO.getAllList();
        listData.clear();
        listData.addAll(accounts);
    }

    @FXML
    public void showPurchaseAccount(ActionEvent actionEvent) {
        List<Account> accounts = iAccountDAO.getAllListByStatus(Account.PURCHASE);
        listData.clear();
        listData.addAll(accounts);
    }

    @FXML
    public void showRefundAccount(ActionEvent actionEvent) {
        List<Account> accounts = iAccountDAO.getAllListByStatus(Account.REFUND);
        listData.clear();
        listData.addAll(accounts);
    }


}
