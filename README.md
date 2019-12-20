# MyDistributedLock
##项目介绍：
分布式锁使用公平锁实现：当多个线程同时请求加锁时，优先分配给先发出请求的线程。

其实简单的分布式锁可以自己实现，大概意思是自定义一个key，加锁成功就是获取key，key对应的value为空，然后向key中写入一个有意义的value，其他线程

技术选型：redis + redission + spring boot

先来了解一下redission：

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。
大家常用的redis客户端是jedis，这里的redisson是另外一种redis的客户端实现，这里采用redission主要是使用它提供的分布式锁。

redission提供的分布式锁：
 可重入锁（Reentrant Lock）、公平锁（Fair Lock）、联锁（MultiLock）、红锁（RedLock）、读写锁（ReadWriteLock）等。
以上信息摘抄自官方文档：https://github.com/redisson/redisson/wiki/

这里我们采用公平锁，其实
