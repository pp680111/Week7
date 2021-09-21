package com.zst.week7.q9.module.order.dao;

import com.zst.week7.q9.module.order.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoQuery extends OrderDao {
    @Autowired
    @Qualifier("slave1JdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Order get(long id) {
        logDataSourceInfo(jdbcTemplate.getJdbcTemplate());

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Order> list = jdbcTemplate.query(SqlConsts.GET, params, rowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    public Order getFirst() {
        logDataSourceInfo(jdbcTemplate.getJdbcTemplate());

        List<Order> list = jdbcTemplate.query(SqlConsts.GET_FIRST, rowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    private RowMapper<Order> rowMapper() {
        return (rs, rowNum) -> {
            try {
                return new Order(rs.getLong("id"), rs.getLong("user_id"),
                        rs.getString("shipping_address"), rs.getInt("status"),
                        rs.getString("price"), rs.getLong("create_time"), rs.getLong("update_time"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}
