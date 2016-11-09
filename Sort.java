import java.util.Arrays;

/**
 * Created by lock on 2016/11/9.
 */

public class Sort {
    /**
     * quick sort java实现
     * @param args
     */
    public static void main(String args[]){
        int[] arr = {12,11,45,6,8,43,40,57,3,20};
        int kv,index,tmp;
        print("sort before\t");
        for(int i :arr){
            print(i+"\t");
        }
        echo("");
        print("sort after \t");
        for(int i=0;i<arr.length;i++){
            index = i;
            kv = arr[i];
            for(int j=i;j<arr.length;j++){
                if(arr[j]<kv){
                    index = j;
                    kv = arr[j];
                }
            }
            tmp = arr[i];
            arr[i] = arr[index];
            arr[index]=tmp;
        }
        for (int i : arr) {
            print(i+"\t");
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
