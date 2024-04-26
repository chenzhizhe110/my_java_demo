package cn.pushu.cache.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> MyCacheConfig<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> 多缓存的配置 <br>
 * <b>date:</b> 2024/4/2314:16
 */
@Configuration
public class MoreCacheConfig {

  //配置自定义的缓存管理器
//    @Bean
//    public CacheManager myCache() {
//        return new MyCacheManager();
//    }

    /**
     * ehcache的实现
     */
    @Bean
    @Primary
    public CacheManager ehCacheManager(){
        net.sf.ehcache.CacheManager cacheManager=EhCacheManagerUtils.buildCacheManager();
        return  new EhCacheCacheManager(cacheManager);
    }

    /**
     * redis的实现
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


}
