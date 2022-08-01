import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

import com.sun.management.OperatingSystemMXBean;
import com.sun.nio.sctp.PeerAddressChangeNotification;

import javax.management.MBeanServerConnection;


public class MultiThread {

    public static void main(String[] args) throws IOException, InterruptedException {

        /**Monitoring processes, getting processes time before extensive stress test begins*/
        MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
        OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
                mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
        long nanoBefore = System.nanoTime();
        long cpuBefore = osMBean.getProcessCpuTime();


        /**To monitor our application-created threads, is it best to use ThreadGroup such that we don't confuse
         * other JVM-create threads (such as the main threads)
         * We also keep track of Threads using an Array, cause ThreadGroup has a terrible design when it comes
         * to iterating threads.
         * */

        ThreadGroup tg1 = new ThreadGroup("ThreadGroup 1");
        List<Thread> tg2 = new ArrayList<>();
        /**
        //initializing FIBO threads
        for (int i = 0;i <8;i++){
            //all our threads will be in threadgroup 1
            Thread t1 = new Thread(tg1,new MultiThread().new RunnableImpl());
            tg2.add(t1);
            t1.start();

            //threads information.
            ThreadMXBean tm = ManagementFactory.getThreadMXBean();
            ThreadInfo ti = tm.getThreadInfo(t1.getId());
            System.out.println(ti);
        }
         **/

        int threadNum = 0;
        //Getting user inputs
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Enter number of threads to create: ");
        threadNum = Integer.parseInt(reader.readLine());


        //using LongAdder because it's thread-safe. Prevent data-race scenario between threads
        LongAdder counter = new LongAdder();
        for(int i = 0; i < threadNum;i++){

            AdderThread thread = new AdderThread(counter);
            Thread t = new Thread(thread);
            tg2.add(t);
            t.start();
        }


        for (int i = 0; i < 15; i++)
        {
            counter.reset();
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                break;
            }
            System.out.printf("[%d] Calculations per second: %d (%d per thread)\n",
                    i,
                    counter.longValue(),
                    counter.intValue()/threadNum
            );
        }
        for (int i = 0;i<threadNum;i++){
            System.out.printf("Stopping thread [%d]\n", i);
            tg2.get(i).stop();
        }


        /**Start monitoring processes after processes begins*/
        long nanoAfter = System.nanoTime();
        long cpuAfter = osMBean.getProcessCpuTime();
        System.out.println("number of application-created threads running: " + tg1.activeCount());

        long cpu_usage = usage(nanoBefore,cpuBefore,nanoAfter,cpuAfter);
        System.out.println("Cpu usage: " + cpu_usage + "%");



    }

    private static long usage(long nanoBefore, long cpuBefore, long nanoAfter, long cpuAfter){
        long percent;
        if (nanoAfter > nanoBefore) {
            //make sure our time is right, since it's arbitrary
            percent = ((cpuAfter - cpuBefore)*100)/(nanoAfter-nanoBefore);
        }else{
            percent = 0;
        }
        return percent;
    }

    private class RunnableImpl implements Runnable {
        private boolean stopped = false;

        @Override
        public void run()
        {
            while (!this.stopped){
                fibo(9000);
            }
        }



        /**Stress testing using fibonacci sequence*/
        public int fibo(int n){
            if (n<=1) return n;
            return fibo(n-1) + fibo(n-2);
        }
        /**A function to stop Threads using a boolean flag. Java default methods such as stop() has been deprecated.*/
        public void stop(){
            this.stopped = true;
        }

    }

    /**Another method to stress test, using long calculation*/
    public static class AdderThread implements Runnable{
        Random rng;
        LongAdder calcP;
        boolean stopped;
        double store;


        public AdderThread(LongAdder x){
            this.calcP = x;
            this.stopped = false;
            this.rng = new Random();
            this.store = 1;
        }

        @Override
        public void run(){
            while(!this.stopped){
                double x = this.rng.nextFloat();
                double y = Math.sin(Math.cos(Math.sin(Math.cos(x))));
                this.store *=y;
                this.calcP.add(1);
            }
        }

        public void stop(){
            this.stopped = true;
        }

    }

}
