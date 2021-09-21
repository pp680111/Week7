package com.zst.week7.q9.module.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private long userId;
    private String shippingAddress;
    private int status;
    private String price;
    private long createTime;
    private long updateTime;
}
