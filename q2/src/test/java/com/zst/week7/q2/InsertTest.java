package com.zst.week7.q2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class InsertTest {
    public void resetTable(DataSource dataSource, String tableName) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()){
            stmt.executeUpdate("TRUNCATE " + tableName);
            stmt.executeUpdate(String.format("ALTER TABLE %s engine=innodb", tableName));
        }
    }
}
