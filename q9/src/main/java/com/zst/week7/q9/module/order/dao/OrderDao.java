package com.zst.week7.q9.module.order.dao;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public abstract class OrderDao {
    public void logDataSourceInfo(JdbcTemplate jdbcTemplate) {
        // 输出jdbcTemplate的datasource的jdbcUrl，判别使用的是主库还是从库
        HikariDataSource dataSource = (HikariDataSource) jdbcTemplate.getDataSource();
        log.info("current dao's datasource jdbcUrl = " + dataSource.getJdbcUrl());
    }
}
