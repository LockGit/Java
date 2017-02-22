import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lock on 2016/11/12.
 */
public class MulitThreadRunnable implements Runnable {
    /**
     * 必须实现run方法
     */
    public void run() {
        for (int row = 1; row < 10; row++) {
            for (int i = 0; i < row; i++) {
                print("*");
            }
            echo("");
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            echo(e.getMessage());
        }
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");
        String time = format.format(date);
        echo("I am sleep , time is: " + time);
    }

    public static void main(String[] args) {
        Runnable obj1 = new MulitThreadRunnable();
        Runnable obj2 = new MulitThreadRunnable();
        Runnable obj3 = new MulitThreadRunnable();
        Thread t1 = new Thread(obj1);
        Thread t2 = new Thread(obj2);
        Thread t3 = new Thread(obj3);
        t1.start();
        t2.start();
        t3.start();
    }


    public static void echo(String msg) {
        System.out.println(msg);
    }

    public static void print(String msg) {
        System.out.print(msg);
    }

}
