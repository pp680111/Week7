package com.zst.week7.q2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Component
public class JDBCBatchInsert {
    @Autowired
    private DataSource dataSource;

    public void insert(String sql, Consumer<PreparedStatement> paramSetter, int batchSize, int times) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prest = conn.prepareStatement(sql)) {
//            conn.setAutoCommit(false);
            for (int i = 0; (times % batchSize != 0 && i <= times / batchSize)
                    || i < times / batchSize; i++) {
                for (int j = 0; j < batchSize; j++) {
                    paramSetter.accept(prest);
                    prest.addBatch();
                }
                prest.executeBatch();
            }
//            conn.commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void concurrentInsert(String sql, Consumer<PreparedStatement> paramSetter, int batchSize, int times) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prest = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (int i = 0; (times % batchSize != 0 && i <= times / batchSize)
                    || i < times / batchSize; i++) {
                for (int j = 0; j < batchSize; j++) {
                    paramSetter.accept(prest);
                    prest.addBatch();
                }
                prest.executeBatch();
            }
            conn.commit();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
