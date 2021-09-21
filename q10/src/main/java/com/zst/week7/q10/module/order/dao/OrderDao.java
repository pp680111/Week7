package com.zst.week7.q10.module.order.dao;

import com.zst.week7.q10.module.order.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface OrderDao {
    @Select("SELECT * from t_order WHERE id = #{id}")
    Order get(long id);

    @Select("SELECT * from t_order LIMIT 1")
    Order getFirst();

    @Insert("INSERT INTO t_order VALUES(#{id}, #{userId}, #{shippingAddress}, #{status}, #{price}, #{createTime}, #{updateTime}) ")
    void insert(Order entity);
}
