package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 利用IO流完成日志功能的记录.
 * */
public class LogUtil {
    private static final String fileName = "ExceptionLog.txt";
    private static FileOutputStream outputStream ;
    private static PrintStream printStream;
    private static SimpleDateFormat simpleDateFormat ;

    static {
        try {
            outputStream = new FileOutputStream(fileName,true);
            printStream = new PrintStream(outputStream);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PrintStream getPrintStream() {
        return printStream;
    }
}
