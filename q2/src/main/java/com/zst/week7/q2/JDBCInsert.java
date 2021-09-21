package com.zst.week7.q2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.function.Function;

@Component
public class JDBCInsert {
    @Autowired
    private DataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insert(String sql, Function<String, String> sqlFormatter, int times) {
        try (Connection conn = dataSource.getConnection();
             Statement statement = conn.createStatement()) {
            for (int i = 0; i < times; i++) {
                if (statement.executeUpdate(sqlFormatter.apply(sql)) <= 0) {
                    throw new Exception("Insert failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
