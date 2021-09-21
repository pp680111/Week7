package com.zst.week7.q2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class JDBCInsertTest extends InsertTest{
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JDBCInsert service;

    @Test
    public void insert1Millons() throws SQLException {
        resetTable(dataSource, "t_order");
        long t = System.currentTimeMillis();
        service.insert("INSERT INTO t_order VALUES(%d, 1, '广东省广州市', 1, '50.21', 1, 1)", sql -> {
            long time = System.currentTimeMillis();
            return String.format(sql, IDGenerator.get(), time, time);
        }, 1_000_000);
        System.out.println(System.currentTimeMillis() - t);
    }
}
