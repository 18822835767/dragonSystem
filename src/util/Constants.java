package util;

/**
 * 存放常量.
 * */
public final class Constants {
    private Constants(){}

    public static class AccountConstant{
        /**
         * 表示购买/退款.
         * */
        public static final String PURCHASE = "购买";
        public static final String REFUND = "退款";
    }

    public static class TicketConstant{
        //票的类型
        public static final String TYPE1 = "一等票";
        public static final String TYPE2 = "二等票";
        public static final String TYPE3 = "三等票";

        //票的价格
        public static final double PRICE1 = 20.0;
        public static final double PRICE2 = 10.0;
        public static final double PRICE3 = 5.0;

        //退票的价格。一次有效次数==一个Back_Price
        public static final double Back_Price = 1.0;

        //票的有效次数
        public static final int TIMES1 = 15;
        public static final int TIMES2 = 5;
        public static final int TIMES3 = 1;
    }
}
