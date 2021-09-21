package com.zst.week7.q2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class JDBCPrepareStatementInsertTest extends InsertTest{
    @Autowired
    private JDBCPrepareStatementInsert service;
    @Autowired
    private DataSource dataSource;

    @Test
    public void run() throws SQLException {
        resetTable(dataSource, "t_order");
        long t = System.currentTimeMillis();
        service.insert("INSERT INTO t_order VALUES(?, 1, '广东省广州市', 1, '50.21', ?, ?)", preSt -> {
            try {
                preSt.setLong(1, IDGenerator.get());
                long time = System.currentTimeMillis();
                preSt.setLong(2, time);
                preSt.setLong(3, time);
            } catch (SQLException e) {
                System.err.println("Param set error");
                e.printStackTrace();
            }
        }, 1_000_000);
        System.out.println(System.currentTimeMillis() - t);
    }

    @Test
    public void runParallel() throws SQLException {
        resetTable(dataSource, "t_order");
        long t = System.currentTimeMillis();
        service.parallelInsert("INSERT INTO t_order VALUES(?, 1, '广东省广州市', 1, '50.21', ?, ?)", preSt -> {
            try {
                preSt.setLong(1, IDGenerator.get());
                long time = System.currentTimeMillis();
                preSt.setLong(2, time);
                preSt.setLong(3, time);
            } catch (SQLException e) {
                System.err.println("Param set error");
                e.printStackTrace();
            }
        }, 1_000_000);
        System.out.println(System.currentTimeMillis() - t);
    }
}
