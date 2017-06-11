package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
//	@Test
//	public void fun1() {
//		Jedis jedis = new Jedis("192.168.25.128",6379);
//		jedis.set("test", "jedis test");
//		System.out.println(jedis.get("test"));
//		jedis.close();
//	}
//	
//	@Test
//	public void fun2() {
//		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
//		Jedis jedis = jedisPool.getResource();
//		System.out.println(jedis.get("test"));
//		jedis.close();
//		jedisPool.close();
//	}
//	
//	@Test
//	public void fun3() {
//		Set<HostAndPort> nodes = new HashSet<>();
//		nodes.add(new HostAndPort("192.168.25.128", 7001));
//		nodes.add(new HostAndPort("192.168.25.128", 7002));
//		nodes.add(new HostAndPort("192.168.25.128", 7003));
//		nodes.add(new HostAndPort("192.168.25.128", 7004));
//		nodes.add(new HostAndPort("192.168.25.128", 7005));
//		nodes.add(new HostAndPort("192.168.25.128", 7006));
//		JedisCluster jedisCluster = new JedisCluster(nodes);
//		System.out.println(jedisCluster.get("username"));
//		jedisCluster.close();
//	}
}
