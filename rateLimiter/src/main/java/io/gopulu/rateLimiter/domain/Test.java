package io.gopulu.rateLimiter.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by lingrajmahanand on 8/3/17.
 */
public class Test {
    protected final ExecutorService executorService = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors(),4));//.newFixedThreadPool(10, "Custom");
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        long time1 = System.currentTimeMillis();
        System.out.println(time1);

        Map<String,RateLimitTracker> trackerMap = new HashMap<>();
        RateLimitTracker tracker1 = new RateLimitTracker(1,2000);
        RateLimitTracker tracker2 = new RateLimitTracker(1,1000);


        trackerMap.put("1",tracker1);
        trackerMap.put("2",tracker2);
        Test test = new Test();

        //given
         CompletableFuture<Boolean> trackerFlag = test.checkIfAPIAccessBeSuspendedforKey(tracker1);
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        trackerFlag = test.checkIfAPIAccessBeSuspendedforKey(tracker1);

         CompletableFuture<Boolean> recovered = trackerFlag
                .handle((res,throwable) -> {
                    if(throwable !=null){
                        //return false;
                        throw new RuntimeException();
                    }else{
                        return res;
                    }
                });



        long time2 = System.currentTimeMillis();
        System.out.println(time2);
        long diff = time2 - time1;
        System.out.println(diff);
        try {
            System.out.println("second call - "+recovered.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        try {
            test.stopPool();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopPool() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
    protected CompletableFuture<Boolean> checkIfAPIAccessBeSuspendedforKey(RateLimitTracker rateLimitTracker){
        return CompletableFuture.supplyAsync(() ->
                        rateLimitTracker.isAccessWithinRateLimit(),
                executorService);
    }

}
