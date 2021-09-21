package com.zst.week7.q2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class JDBCMultiValueInsert {
    @Autowired
    private DataSource dataSource;

    public void insert(String sqlPrefix, Supplier<String> valuesSupplier, int batchSize, int times) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (int i = 0; i <= times / batchSize; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(sqlPrefix);
                for (int j = 0; j < Math.min(times - (i * batchSize), batchSize); j++) {
                    sb.append(valuesSupplier.get());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                stmt.executeUpdate(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parallelInsert(String sqlPrefix, Supplier<String> valuesSupplier, int batchSize, int times) throws InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        int numsPerThread = times / threads;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int c = 0; c <= threads; c++) {
            int gap = Math.min(times, numsPerThread * (c + 1)) - c * numsPerThread;
            pool.submit(() -> {
                try (Connection conn = dataSource.getConnection();
                     Statement stmt = conn.createStatement()) {
                    for (int i = 0; i <= gap / batchSize; i++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(sqlPrefix);
                        for (int j = 0; j < Math.min(gap - (i * batchSize), batchSize); j++) {
                            sb.append(valuesSupplier.get());
                            sb.append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        stmt.executeUpdate(sb.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
