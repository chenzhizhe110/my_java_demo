package cn.pushu.cache.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> MyCacheConfig<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> 多缓存的配置 <br>
 * <b>date:</b> 2024/4/2314:16
 */
@Configuration
public class MoreCacheConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private int port;
    @Autowired
    private Environment env;

    //配置自定义的缓存管理器
//    @Bean
//    public CacheManager myCache() {
//        return new MyCacheManager();
//    }

    /**
     *
     * 配置ehCache cacheManager的实现
     */
    @Bean
    @Primary
    public CacheManager ehCacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }


    /**
     * redis的 cacheManager 实现
     *
     * @param redisConnectionFactory
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // .entryTtl(Duration.ofMinutes(10)); // 设置 Redis 缓存过期时间为10分钟
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    /**
     * <p>
       description:配置redis的连接工厂<br>
     　 author chenzhizhe<br>
     　 date 2024/4/28 10:41 <br>
     *<p/>
     　* @param
     　 @return org.springframework.data.redis.connection.RedisConnectionFactory
    　*/
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
            RedisStandaloneConfiguration redisStandaloneConfiguration =
                    new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(host);
            redisStandaloneConfiguration.setDatabase(0);
            if(password!=null&&!"".equals(password)) {
                redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
            }
            redisStandaloneConfiguration.setPort(port);
           // JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

          JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder().usePooling();
          jedisClientConfiguration.poolConfig(jedisPoolConfig());
          JedisConnectionFactory jedisConnectionFactory= new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
          jedisConnectionFactory.afterPropertiesSet();
          return  jedisConnectionFactory;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(env.getProperty("spring.redis.jedis.pool.max-active")));
        poolConfig.setMinIdle(Integer.parseInt(env.getProperty("spring.redis.jedis.pool.min-idle")));
        poolConfig.setMaxWaitMillis(Long.parseLong(env.getProperty("spring.redis.jedis.pool.max-wait")));
        return poolConfig;
    }

}
