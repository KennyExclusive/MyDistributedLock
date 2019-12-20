# MyDistributedLock
##项目介绍：
redis分布式锁使用公平锁实现：当多个线程同时请求加锁时，优先分配给先发出请求的线程。

这里啰嗦一下，其实简单的分布式锁多是乐观锁，大概意思是自定义一个key，加锁成功的表现是：在redis中获取key对应的value为空，然后向key中写入一个有意义的value，这里的读取和写入要使用lua脚本保证原子性，其他线程读取key时，发现key有值，就算作加锁失败，此时就有意思了，加锁失败的线程是阻塞等待（或者是不断重试）还是直接返回结果给上游？也就是这种分布式锁机制，是乐观锁还是悲观锁？如果是阻塞等待，那么持有锁的线程解锁了，如何通知正在阻塞的其他线程，通知哪一个？

基于以上原因，本人采用的redisson实现的公平锁，它是一种悲观锁，请求线程会进入队列，阻塞时采用不断重试的机制，可以指定重试次数，再加上redisson的看门狗机制，已经可以给我们提供一个很完善的锁工具，如果不考虑公平性，可采用可重入锁。

技术选型：redis + redisson + spring boot

先来了解一下redisson：

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。
大家常用的redis客户端是jedis，这里的redisson是另外一种redis的客户端实现，这里采用redission主要是使用它提供的分布式锁。

redission提供的分布式锁：
 可重入锁（Reentrant Lock）、公平锁（Fair Lock）、联锁（MultiLock）、红锁（RedLock）、读写锁（ReadWriteLock）等。
以上信息摘抄自官方文档：https://github.com/redisson/redisson/wiki/

