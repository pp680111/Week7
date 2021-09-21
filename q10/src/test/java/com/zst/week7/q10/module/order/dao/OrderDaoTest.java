package com.zst.week7.q10.module.order.dao;

import com.zst.week7.q10.module.order.entity.Order;
import com.zst.week7.q10.utils.IDGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testGetFirst() {
        Order entity = orderDao.getFirst();
        Assertions.assertNotNull(entity);
        System.out.println(entity.toString());
    }

    @Test
    public void testInsert() {
        long id = IDGenerator.get();

        Order entity = new Order();
        entity.setId(id);
        entity.setUserId(1L);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setPrice("100.00");
        entity.setShippingAddress("广东省广州市天河区");
        entity.setStatus(1);

        orderDao.insert(entity);

        Order newEntity = orderDao.get(id);
        Assertions.assertNotNull(newEntity);
        Assertions.assertEquals(entity.toString(), newEntity.toString());
        System.err.println(newEntity);
    }
}
