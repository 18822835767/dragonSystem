package entity;

/**
 * 账目实体类.
 * */
public class Account {
    private int accountId;
    private int foreignerId;
    private double money;
    private String createTime;
    private String status;//表示状态，即购买/退货.

    /**
     * 表示购买/退款.
     * */
    public static final String PURCHASE = "购买";
    public static final String REFUND = "退款";

    public Account(int accountId, int foreignerId, double money, String createTime, String status) {
        this.accountId = accountId;
        this.foreignerId = foreignerId;
        this.money = money;
        this.createTime = createTime;
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public void setForeignerId(int foreignerId) {
        this.foreignerId = foreignerId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
