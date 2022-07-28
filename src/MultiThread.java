import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;


public class MultiThread {
    int threadNum;

    public static void main(String[] args) throws IOException {

        /**Monitoring processes, getting processes time before extensive stress test begins*/
        MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
        OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
                mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
        long nanoBefore = System.nanoTime();
        long cpuBefore = osMBean.getProcessCpuTime();



        //initializing threads
        for (int i = 0;i <8;i++){
            Thread t1 = new Thread(new MultiThread().new RunnableImpl());
            t1.start();

            //threads information.
            ThreadMXBean tm = ManagementFactory.getThreadMXBean();
            ThreadInfo ti = tm.getThreadInfo(t1.getId());
            System.out.println(ti);
        }

        /**Start monitoring processes after processes begins*/

        long nanoAfter = System.nanoTime();
        long cpuAfter = osMBean.getProcessCpuTime();

        System.out.println(Thread.activeCount());


    }

    private long usage(long nanoBefore, long cpuBefore, long nanoAfter, long cpuAfter){
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

        public void run()
        {
            System.out.println(Thread.currentThread().getName()
                    + ", executing run() method!");
            fibo(9000);
        }
        /**Stress testing using fibonacci sequence*/
        public int fibo(int n){
            if (n<=1) return n;
            return fibo(n-1) + fibo(n-2);
        }
        //r


    }

}
