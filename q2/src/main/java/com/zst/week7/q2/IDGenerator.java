package com.zst.week7.q2;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    static final AtomicInteger suffix = new AtomicInteger(0);
    static final int SUFFIX_LENGTH  = 32;

    static long get() {
        return (System.currentTimeMillis() << SUFFIX_LENGTH)
                | suffix.incrementAndGet();
    }
}
