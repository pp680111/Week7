package com.zst.week7.q9.module.order.dao;

public interface SqlConsts {
    String GET = "SELECT * FROM t_order WHERE id = :id";
    String GET_FIRST = "SELECT * FROM t_order LIMIT 1";
    String INSERT = "INSERT INTO t_order VALUES(:id, :userId, :shippingAddress, :status, :price, :createTime, :updateTime)";
}
