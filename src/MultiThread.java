public class MultiThread {
    int threadNum;

    public static void main(String[] args){
        System.out.println("Main thread is- "
                + Thread.currentThread().getName());
        for (int i = 0;i <2;i++){
            Thread t1 = new Thread(new MultiThread().new RunnableImpl());
            t1.start();
        }
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
