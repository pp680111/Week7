package com.zst.week7.q2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.IntStream;

public class IDGeneratorTest {
    @Test
    public void testGet() {
        for (int i = 0; i < 10; i++) {
            System.out.println(IDGenerator.get());
        }
    }

    @Test
    public void testGetPerformance() {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            IDGenerator.get();
        }
        System.err.println(System.currentTimeMillis() - t);
    }

    @Test
    public void testGetIdIsDistinct() {
        Set<Long> s = new HashSet<>(1000000);
        for (int i = 0; i < 1_000_000; i++) {
            s.add(IDGenerator.get());
        }

        System.err.println(s.size());
        Assertions.assertEquals(1_000_000, s.size());
    }

    @Test
    public void testParallelGetIdIsDistinct() {
        Set<Long> s = new ConcurrentSkipListSet<>();
        IntStream.range(0, 1_000_000).parallel().forEach(i -> s.add(IDGenerator.get()));
        System.err.println(s.size());
        Assertions.assertEquals(1_000_000, s.size());
    }

}
