import java.util.concurrent.atomic.LongAdder;

public class test {
    static LongAdder x = new LongAdder();

    public static void main(String[] args){
        System.out.println(x);
        x.add(1);
        System.out.println(x);

    }

}
