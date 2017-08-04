package io.gopulu.rateLimiter.domain;


import sun.misc.Contended;

import java.util.TreeSet;

public class RateLimitTracker {
    @Contended
    private volatile TreeSet timeSet = new TreeSet();

    private int rateLimit;


    private long accessTimeWindow;

    public RateLimitTracker(int rateLimit, long accessTimeWindow) {
        this.rateLimit = rateLimit;
        this.accessTimeWindow = accessTimeWindow;
    }

    public boolean isAccessWithinRateLimit() {
        long now = System.currentTimeMillis();
        long earliest = now - this.accessTimeWindow;

        while (!this.timeSet.isEmpty()) {
            Long earliestTimestamp = (Long) this.timeSet.first();
            if (earliestTimestamp.longValue() <= earliest) {
                this.timeSet.remove(earliestTimestamp);
            } else {
                break;
            }
        }

        if (this.timeSet.size() >= this.rateLimit) {
            return false;
        }


        this.timeSet.add(new Long(now));

       // System.out.println("is time set empty after add: "+timeSet.isEmpty());

        /*try {
            System.out.println(Thread.currentThread().getName());
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return true;
    }
}
