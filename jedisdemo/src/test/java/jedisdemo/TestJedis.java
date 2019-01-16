package jedisdemo;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedisAdd(){
		
		Jedis jedis = new Jedis("192.168.0.137",6379);
		jedis.set("hello", "world");
		
		String result = jedis.get("hello");
		System.out.println(result);
		jedis.close();
	}
	
	@Test
	public void testJedisPoolAdd(){
		JedisPool jedisPool = new JedisPool("192.168.0.137",6379);
		Jedis jedis = jedisPool.getResource();
        jedis.set("hello1", "world1");
		
		String result = jedis.get("hello1");
		System.out.println(result);
		jedisPool.close();
		
	}

}
