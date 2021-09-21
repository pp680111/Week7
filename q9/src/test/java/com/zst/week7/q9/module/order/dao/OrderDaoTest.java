package com.zst.week7.q9.module.order.dao;

import com.zst.week7.q9.module.order.entity.Order;
import com.zst.week7.q9.utils.IDGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderDaoTest {
    @Autowired
    private OrderDaoInsert insertDao;
    @Autowired
    private OrderDaoQuery queryDao;

    @Test
    public void testGetFirst() {
        Order entity = queryDao.getFirst();
        Assertions.assertNotNull(entity);
        System.err.println(entity.toString());
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
        insertDao.insert(entity);

        // 读写一致性问题，从主库读取
        Order newEntity = insertDao.get(id);
        Assertions.assertNotNull(newEntity);
        Assertions.assertEquals(entity.toString(), newEntity.toString());
        System.err.println(newEntity);

        // 此时应该已经同步到从库了，从从库读取
        newEntity = queryDao.get(id);
        Assertions.assertNotNull(newEntity);
        Assertions.assertEquals(entity.toString(), newEntity.toString());
        System.err.println(newEntity);
    }
}
