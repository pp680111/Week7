CREATE TABLE `t_user` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '用户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `t_item` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `status` int NOT NULL COMMENT '状态',
  `description` varchar(255) NOT NULL COMMENT '商品描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品';

CREATE TABLE `t_item_stock` (
    `id` bigint NOT NULL,
    `item_id` bigint NOT NULL COMMENT '商品id',
    `mark` varchar(255) NOT NULL COMMENT '商品库存标识',
    `price` varchar(20) NOT NULL COMMENT '价格',
    `stockNum` int NOT NULL COMMENT '库存量',
    `status` int NOT NULL COMMENT '状态',
    PRIMARY KEY (`id`),
    KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存';

CREATE TABLE `t_order` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '订单所属用户id',
  `shipping_address` varchar(255) not null comment '用户收货地址',
  `status` int NOT NULL COMMENT '订单状态',
  `price` varchar(50) NOT NULL COMMENT '总价',
  `create_time` bigint COMMENT '创建时间',
  `update_time` bigint COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

CREATE TABLE `t_order_item` (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL COMMENT '订单id',
  `item_id` bigint NOT NULL COMMENT '商品id',
  `item_stock_id` bigint NOT NULL COMMENT '商品库存id',
  `price` varchar(20) NOT NULL COMMENT '价格',
  `num` int(11) NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品关联表';