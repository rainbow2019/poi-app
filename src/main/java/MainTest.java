import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainTest {

    @Test
    public void CountDownTest(){
        Executor executor = Executors.newFixedThreadPool(2);
        final CountDownLatch countDownLatch = new CountDownLatch(4);
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("任务1完成...");
                countDownLatch.countDown();
                System.out.println("任务2完成...");
                countDownLatch.countDown();
                System.out.println("任务3完成...");
                countDownLatch.countDown();
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("等待所有任务执行完毕......");
                try {
                    countDownLatch.await();
                    System.out.println("count = " + countDownLatch.getCount());
                }catch(Exception e){
                    System.out.println("异常e = " + e);
                }
                System.out.println("所有任务已经执行完毕了");

            }
        };
        executor.execute(task2);
        executor.execute(task1);
        executor.execute(task1);
        executor.execute(task1);
        executor.execute(task2);
        executor.execute(task2);
        executor.execute(task2);
        try {
            Thread.currentThread().sleep(10000);
        }catch(Exception e){

        }
    }


}
