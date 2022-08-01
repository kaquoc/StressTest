import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.LongAdder;

public class test {
    static LongAdder x = new LongAdder();

    public static void main(String[] args) throws IOException {
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("please enter your name ");
        // Reading data using readLine
        String name = reader.readLine();

        // Printing the read line
        System.out.println(name);
    }

}
