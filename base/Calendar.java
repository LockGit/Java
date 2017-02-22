/**
 * Created by lock on 2016/11/10.
 */
import java.util.*;
public class Calendar {

    public static void main(String[] args){
        Date date = new Date();
        GregorianCalendar now = new GregorianCalendar();
        echo(date.toString());
        now.setTime(date);
        int today = now.get(now.DAY_OF_MONTH);
        int month = now.get(now.MONTH);
        //得到 now 是一周的第几天
        int week = now.get(now.DAY_OF_WEEK);
        //打印出前面的空格
        for (int i = now.SUNDAY; i < week; i++) {
            print("\t");
        }

        while (now.get(now.MONTH) == month) {
            int day = now.get(now.DAY_OF_MONTH); //为了对齐要对大于 10 和小于 10 的数打印不同空格数
            //周六换行
            if (week == now.SATURDAY) {
                echo(" "+day+"");
            }
            if(day==today){
                print("(" + day + ")");
            }else if((week != now.SATURDAY)){
                print(" " + day + " ");
            }
            //增加一天
            now.add(now.DAY_OF_MONTH, 1);
            week = now.get(now.DAY_OF_WEEK);
        }
    }

    public static void echo(String message){
        System.out.println(message);
        return;
    }

    public static void print(String message){
        System.out.print(message);
        return;
    }
}
