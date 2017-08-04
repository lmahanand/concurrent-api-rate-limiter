package io.gopulu.rateLimiter;


import io.gopulu.rateLimiter.domain.RateLimitTracker;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class RateLimitTrackerTest {
    @Test
    public void testisCallOK(){
        Map<String,RateLimitTracker> trackerMap = new HashMap<>();
        RateLimitTracker tracker1 = new RateLimitTracker(1,1000);
        RateLimitTracker tracker2 = new RateLimitTracker(1,1000);


        trackerMap.put("1",tracker1);
        trackerMap.put("2",tracker2);

        ExecutorService es = Executors.newFixedThreadPool(2);

        Runnable runnable1 = () -> {
            //System.out.println(trackerMap.get("1").isCallOK());
           // System.out.println("tracker1 => "+ trackerMap.get("1").isCallOK());

        };

        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable1);

        t1.start();
        t2.start();

//        IntStream.range(0,1)
//                .forEach(i -> es.submit(t1));

        for(int i=0;i<2;i++){
            System.out.println("sub 1");
            es.submit(t1);
            System.out.println("sub 2");
        }

        IntStream.range(0,1)
                .forEach(i -> es.submit(t2));

        //es.shutdown();

    }
}
