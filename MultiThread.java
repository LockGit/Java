import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lock on 2016/11/11.
 */

public class MultiThread extends Thread {

    /**
     * 线程名字，可以不实现
     * @param tName
     */
    public MultiThread(String tName){
        super(tName);
    }
    /**
     * 继承 Thread 类 ，并且实现run方法
     */
    public void run(){
        for (int row = 1; row < 10; row++){
            for (int i = 0; i < row; i++) {
                print("*");
            }
            echo("");
        }
        try {
            sleep(2000);
        }catch (Exception e){
            echo(e.getMessage());
        }
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");
        String time = format.format(date);
        echo("I am sleep , time is: "+time+" thread name is:"+this.getName());
    }

    public static void main(String args[]){
        MultiThread t1 = new MultiThread("a");
        MultiThread t2 = new MultiThread("b");
        MultiThread t3 = new MultiThread("c");
        t1.start();
        t2.start();
        t3.start();
    }

    public static void echo(String msg){
        System.out.println(msg);
    }

    public static void print(String msg){
        System.out.print(msg);
    }
}


