/**
 * 
 */
package com.kenny.demo.distrilock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置文件
 * @author kenny
 *
 */
@Configuration
public class RedissonConfig {

	@Bean
	public RedissonClient getRedisson() {
		Config config = new Config();
		//单机模式  依次设置redis地址和密码
		SingleServerConfig useSingleServer = config.useSingleServer();
		useSingleServer.setAddress("redis://127.0.0.1:6379");
//		useSingleServer.setPassword("");
		//默认编码为org.redisson.codec.JsonJacksonCodec 
		//改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
//		config.setCodec(new org.redisson.client.codec.StringCodec());
		config.setNettyThreads(100);
		return Redisson.create(config);
	}
}
