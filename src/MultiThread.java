import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;


public class MultiThread {
    int threadNum;

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
        //initializing threads
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

        /**Start monitoring processes after processes begins*/

        long nanoAfter = System.nanoTime();
        long cpuAfter = osMBean.getProcessCpuTime();

        Thread.sleep(7000); //sleep for 5 seconds after program starts.
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
    public class AdderThread implements Runnable{
        Random rng;
        LongAdder calcP;
        boolean stopped;
        double store;
    }

}
