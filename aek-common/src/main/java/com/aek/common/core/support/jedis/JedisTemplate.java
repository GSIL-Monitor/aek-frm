package com.aek.common.core.support.jedis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisTemplate implements ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger();

	private static ShardedJedisPool shardedJedisPool = null;

    @Value("${redis.expiration}")
    private Integer expiration;

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	// 获取线程
	private ShardedJedis getJedis() {
		if (shardedJedisPool == null) {
			synchronized (expiration) {
				if (shardedJedisPool == null) {
					shardedJedisPool = applicationContext.getBean(ShardedJedisPool.class);
				}
			}
		}
		return shardedJedisPool.getResource();
	}

	public <K> K run(String key, Executor<K> executor, Integer... expire) {
		ShardedJedis jedis = getJedis();
		if (jedis == null) {
			return null;
		}
		try {
			K result = executor.execute(jedis);
			if (jedis.exists(key)) {
				if (expire == null || expire.length == 0) {
					jedis.expire(key, expiration);
				} else if (expire.length == 1) {
					jedis.expire(key, expire[0]);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public <K> K run(byte[] key, Executor<K> executor, Integer... expire) {
		ShardedJedis jedis = getJedis();
		if (jedis == null) {
			return null;
		}
		try {
			K result = executor.execute(jedis);
			if (jedis.exists(key)) {
				if (expire == null || expire.length == 0) {
					jedis.expire(key, expiration);
				} else if (expire.length == 1) {
					jedis.expire(key, expire[0]);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public Integer getExpiration() {
		return expiration;
	}

	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}
}
