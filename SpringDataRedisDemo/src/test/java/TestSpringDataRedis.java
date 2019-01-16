import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-redis.xml")
public class TestSpringDataRedis {

	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Test
	public void testRedis(){
		/*redisTemplate.boundValueOps("hello2").set("world2");
		String result = (String) redisTemplate.boundValueOps("hello2").get();
	    System.out.println(result);*/
	    redisTemplate.delete("hello2");
	}
	
}
