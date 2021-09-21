package com.zst.week7.q2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class JDBCMultiValueInsertTest extends InsertTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JDBCMultiValueInsert service;

    @Test
    public void run() throws SQLException {
        resetTable(dataSource, "t_order");
        long t = System.currentTimeMillis();
        service.insert("INSERT INTO t_order VALUES", () -> {
            long time = System.currentTimeMillis();
            return String.format("(%d, 1, '广东省广州市', 1, '50.21', %d, %d)", IDGenerator.get(), time, time);
        }, 500, 1_000_000);

        System.out.println(System.currentTimeMillis() - t);
    }

    @Test
    public void runParallel() throws SQLException, InterruptedException {
        resetTable(dataSource, "t_order");
        long t = System.currentTimeMillis();
        service.parallelInsert("INSERT INTO t_order VALUES", () -> {
            long time = System.currentTimeMillis();
            return String.format("(%d, 1, '广东省广州市', 1, '50.21', %d, %d)", IDGenerator.get(), time, time);
        }, 4000, 1_000_000);

        System.out.println(System.currentTimeMillis() - t);
    }
}
