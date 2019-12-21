package com.kenny.demo.distrilock.controller;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kenny.demo.distrilock.data.MyResultCode;
import com.kenny.demo.distrilock.entity.Order;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单接口
 * @author kenny
 *
 */
@RestController
@Slf4j
public class MyController {

    /**
     * 库存数量
     */
    private static volatile int STOCK_QUANTITY = 100000;
	
	@Autowired
	RedissonClient redisson;

	@PostMapping("/order")
	MyResultCode genGoodOrder(@RequestBody()Order good) {
	    //预减库存
		MyResultCode checkStockResult = checkStock(good);
		if(checkStockResult == MyResultCode.SuccessStock) {
		    //减库存成功，生成订单
		    insertGoodToDataBase(good);
		    checkStockResult = MyResultCode.Success;
		}
		return checkStockResult;
	}
	

	/**
	  * 校验库存
     * @param good  商品信息
     * @return 接口结果信息封装类
     */
	private MyResultCode checkStock(Order good) {
		if(good == null || StringUtils.isEmpty(good.getGoodName())) {
		    //商品信息为空直接返回
			return MyResultCode.PARAM_IS_BLANK;
		}
		//自定义的key
		String goodLockKey = good.getGoodName();
		//公平锁
		RLock fairLock = redisson.getFairLock(goodLockKey);
		try {
		    //尝试加锁，最多阻塞10秒，上锁以后20秒自动解锁
			boolean res = fairLock.tryLock(10,20,TimeUnit.SECONDS);
			if(!res) {
			    //加锁失败
				return MyResultCode.PROCESS_FAULT;
			}
			//预减库存 这里只是模拟一下，实际库存应该是从数据库或者本地缓存中读取
			int newStock = STOCK_QUANTITY - 1;
			if(newStock < 1) {
				return MyResultCode.PROCESS_EMPTY;
			}
			//更新库存，这里只是模拟一下，实际写库存应该考虑写入数据库并且更新本地缓存
			STOCK_QUANTITY = newStock;
			log.info("减1个库存，库存变为[{}]",STOCK_QUANTITY);
			//释放锁
			fairLock.unlock();
			return MyResultCode.SuccessStock;
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("系统出现异常[{}]",e.getMessage());
		} 
		
		if(fairLock != null ) {
		    fairLock.unlockAsync();
		}
		
		return MyResultCode.PROCESS_ERROR;
	}
	
	/**
	 * 推送消息队列，异步生成订单
	 * @param good
	 */
	private void insertGoodToDataBase(Order good) {
		good.setCreateTime(new Date());
		//发送mq do someing
	}
}
