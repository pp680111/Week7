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
public class OrderDaoInsert extends OrderDao {
    @Autowired
    @Qualifier("masterJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(Order entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        logDataSourceInfo(jdbcTemplate.getJdbcTemplate());

        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("userId", entity.getUserId());
        params.put("shippingAddress", entity.getShippingAddress());
        params.put("status", entity.getStatus());
        params.put("price", entity.getPrice());
        params.put("createTime", entity.getCreateTime());
        params.put("updateTime", entity.getUpdateTime());

        int result = jdbcTemplate.update(SqlConsts.INSERT, params);
        if (result == 0) {
            throw new RuntimeException("Insert failed, " + entity.toString());
        }
    }

    public Order get(long id) {
        logDataSourceInfo(jdbcTemplate.getJdbcTemplate());

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Order> list = jdbcTemplate.query(SqlConsts.GET, params, rowMapper());
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
