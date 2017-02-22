/**
 * Created by lock on 2016/11/9.
 */
public class Mul {

    public static void main(String[] args){
        echo("args length eq "+args.length);
        Mul obj = new Mul();
        obj.mulTest();
    }

    public void mulTest(){
        echo("demo");
        print("\t");
        for(int i=1;i<=9;i++){
            print(i+"\t");
        }
        echo("");
        for(int m=1;m<=9;m++){
            print(m+"\t");
            for(int n=1;n<=m;n++){
                print(m*n+"\t");
            }
            echo("");
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
