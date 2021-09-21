package com.zst.week7.q2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Component
public class JDBCPrepareStatementInsert {
    @Autowired
    private DataSource dataSource;

    public void insert(String sql, Consumer<PreparedStatement> paramSetter, int times) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preSt = conn.prepareStatement(sql)){
            for (int i = 0; i < times; i++) {
                paramSetter.accept(preSt);
                if (preSt.executeUpdate() <= 0) {
                    throw new Exception("Insert failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parallelInsert(String sql, Consumer<PreparedStatement> paramSetter, int times) {
        ThreadLocal<PreparedStatement> tlPreSt = new ThreadLocal<>();
        ThreadLocal<Connection> tlConn = new ThreadLocal<>();
        IntStream.range(0, times).parallel().forEach(i -> {
            try {
                PreparedStatement preSt;
                Connection conn;
                if (tlConn.get() == null) {
                    conn = dataSource.getConnection();
                    tlConn.set(conn);
                } else {
                    conn = tlConn.get();
                }

                if (tlPreSt.get() == null) {
                    preSt = conn.prepareStatement(sql);
                    tlPreSt.set(preSt);
                } else {
                    preSt = tlPreSt.get();
                }
                paramSetter.accept(preSt);
                preSt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
