## 测试环境条件

订单表DDL：

```
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
```

硬件环境：

* CPU AMD Ryzen 3600 6c12t
* 内存 8G * 2 @ 3600Mhz
* 硬盘 WD SN500

软件环境：

* Win10
* MySQL 5.7.34
* Docker for windows

## 测试数据

以下部分插入方式在执行时同时使用了docker的MySQL容器环境和直接安装在操作系统中的MySQL环境，如果没有说明都是使用的直接安装的MySQL环境，两种环境的测试数据一并列出。

docker容器的MySQL环境，经测试与主机的颜值在7ms左右。

1. 单线程插入，SQL由字符串拼接生成，一次插入一行数据
    在win10系统的docker容器环境下，插入过程总共花费了2160709ms，将近36分钟
    在直接安装的环境下，插入过程总共花费了418412ms，大概是7分钟
    
2. 单线程PrepareStatement插入，一次插入一行数据
    在win10系统的docker容器环境下，插入过程总共花费了2167014ms，将近36分钟，相比直接SQL插入没有明显的性能差距
    在win10系统直接安装的环境下，插入过程总共花费了417979ms，大约7分钟
    
3. 多线程PrepareStatement插入，一次插入一行数据

    使用12个线程执行插入过程，12个线程共用一个Connection，总共花费了1178822ms，大约19分钟，比起单线程反而更慢了。

    使用12个线程执行插入过程，12个线程各自使用独立的Connection，总共花费了65062ms，大概是1分钟，相比单线程的PrepareStatement插入时间缩短了6/7

4. 单线程PrepareStatement插入，一次插入多个value

    尝试多种拼接的value数量，得到的插入过程耗费时间如下

    | 一次拼接的value数量 | 插入过程耗时 |
    | ------------------- | ------------ |
    | 10000               | 10274ms      |
    | 5000                | 9929ms       |
    | 3000                | 10029ms      |
    | 1000                | 10048ms      |
    | 500                 | 11013ms      |
    | 100                 | 13529ms      |

    可以看到一次性拼接的value数量越高，插入过程耗时越短，且耗时的缩短是有一定的上限的，大约在500~1000个拼接value数量之间。

5. 多线程JDBC PrepareStatement插入，一次插入多个value

    尝试多种拼接的value数量，得到的插入过程耗费时间如下

    | 一次拼接的value数量 | 插入过程耗时 |
    | ------------------- | ------------ |
    | 500                 | 5833ms       |
    | 1000                | 5408ms       |
    | 2000                | 4950ms       |
    | 4000                | 4768ms       |

    除去误差因素，可以认为多线程的插入耗时缩短的上限大约在1000~2000个拼接value数量之间。

6. 单线程PrepareStatement插入，使用addBatch添加多行数据
    在win10系统的docker容器环境下，插入过程总共花费了2348384ms
    在直接安装MySQL环境下，插入过程总共花费了377078ms

    在数据库url中添加了rewriteBatchedStatements=true参数之后，插入过程总共花费了8828ms

7. 单线程PrepareStatement插入，使用addBatch添加多行数据，关闭autoCommit，等100w条数据执行完毕再手动commit

    插入过程总共花费了104135ms

8. 单线程PrepareStatement插入，一次插入一行数据，去除索引和约束
   在直接安装的MySQL环境下，插入过程总共耗时377107ms

9. 单线程PrepareStatement插入，使用addBatch添加多行数据，去除索引和约束
   在直接安装的MySQL环境下，插入过程总共耗时364327ms

10. 多线程PrepareStatement插入，一次插入多个value，去除索引和约束
    在直接安装的MySQL环境下，插入过程总共耗时8584ms

11. 数据处理为文件，通过load data导入

    通过`SELECT ... INTO OUTFILE`语句将100w条订单数据导出为csv文件，然后使用`LOAD DATA INFILE`语句导入，执行时间为4.65秒

    

## 总结

根据上面的测试数据可以看出，大量数据的插入效率，主要受到以下几点的影响

1. 客户端与MySQL服务器之间的网络延迟
2. 客户端与MySQL服务器之间的交互次数
3. MySQL服务端更新数据的成本

#### 网络延迟

网络延迟对于插入效率的影响是很大的，通过对比docker容器环境与本地MySQL环境插入数据耗时的对比可以看出，在单次插入一行数据时，每一次向MySQL服务器发送数据都会受到网络延迟的影响，结果在执行了100W次之后与本地环境的耗时的差距大概在7倍左右。因此客户端与MySQL服务器之间的网络延迟应当尽可能的小。

#### 交互次数

从上面的数据可以看出，插入数据时与数据库的交互次数对于插入时间影响极其巨大。以单线程PrepareStatement一次插入一行数据与一次拼接多个value插入多行数据对比，前者平均时间大约为1170000ms，后者平均时间大约为10000ms，足足相差了117倍，非常的夸张。至于addBatch，在默认的情况下其实是串行执行addBatch添加进去的每一行数据，在添加了rewriteBatchedStatements=true参数之后JDBC驱动会将一批的数据转换为多个value拼接的形式，所以他们之间的插入耗时差距本质上也是交互次数之间的差距。

查阅了MySQL官方文档中之后发现，MySQL给出了一次Insert操作中各项子操作的成本：

* 建立连接：3
* 发送请求数据到MySQL服务器：2
* 解析SQL语句：2
* 插入行：1*行数量
* 更新索引：1\*行数量\*索引数量
* 关闭连接：1

结合上面的数据分析，100W行数据共用一个连接，每次只插入一行与每次插入5000行数据在发送请求数据和解析SQL语句这块的成本差距是非常巨大的。

因此，插入数据时应尽量减少与MySQL的交互次数。

#### 数据处理成本

##### 索引更新与数据约束的成本

在大批量的数据插入时，如果表上面存在一些约束和索引的话，那么每插入一行数据就会处理一次索引和约束，这个成本也是不可忽视的。从测试数据可以看出，单线程PrepareStatement一次插入一行数据未去除索引约束与去除了索引约束的耗时，前者是417979ms，后者是377107ms，大约缩短了10%的插入耗时。

##### 事务成本

在单线程PrepareStatement一次插入一行数据的情况下，关掉autoCommit把100W条数据的插入当作一个事务，与每一行数据插入都作为一个事务处理的耗时差距也是很大的。从单线程PrepareStatement+batchUpdate的耗时对比上可以看出，关掉autoCommit是104135ms，开启时是377078ms，大约缩短了73%的时间。

具体原因是每次事务提交时，都会刷redo log和binlog到磁盘中，尽管有组提交等优化策略，但是这个操作所带来的性能损耗还是非常大的。

但是把大批量的数据当作单一的事务去处理也有一定的弊端，那就是插入过程中数据库服务器崩溃的话，那么那些已经插入的数据就作废了，下次数据库恢复时还是得重新完整插入所有的数据。这个可以在使用的时候再根据实际情况进行取舍。