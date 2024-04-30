package cn.pushu.cache.service;

import cn.pushu.cache.entity.PushTimeBar;
import cn.pushu.cache.entity.ScriptInvokDataSourceParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> KlineMorelevelCacheImpl<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> k线的多线缓存的实现,通过注入不同的CacheManager来实现不同的级存<br>
 * <b>date:</b> 2024/4/25 15:58
 */
@Service
public class KlineMoreLevelCacheImpl implements IKlineDataSourceCache {
    private final Logger log= LoggerFactory.getLogger(KlineMoreLevelCacheImpl.class);
    @Resource
    @Qualifier("redisCacheManager")
    private CacheManager redisCacheManager;

    @Resource
    private CacheManager defaultCacheManager;
    @Resource
    private KlineDataSourceEhCacheImpl klineDataSourceEhCacheImpl;
    @Resource
    private ApplicationContext applicationContext;

    @Value("${moreLevel.enable}")
    private  boolean moreLevelEnable;
    @Value("${moreLevel.redisData}")
    private String redisData;
    private Set<String> redisDataSet=new HashSet<>();

    private static Map<String, KlineDataSourceEhCacheImpl> dataSourceMap = new HashMap<>();
    @PostConstruct
    public void initialize() {
        if(moreLevelEnable){
            KlineDataSourceEhCacheImpl defaultCache = applicationContext.getBean(KlineDataSourceEhCacheImpl.class);
            defaultCache.setCacheManager(defaultCacheManager);
            KlineDataSourceEhCacheImpl redisCache = applicationContext.getBean(KlineDataSourceEhCacheImpl.class);
            redisCache.setCacheManager(redisCacheManager);

            // 可以将这些实例放入 Map 或其他数据结构中
            dataSourceMap.put("defaultCache", defaultCache);
            dataSourceMap.put("redisCache", redisCache);
            String[] split = redisData.split(",");
            redisDataSet.addAll(Arrays.asList(split));
            log.info("MoreLevelCache initialize finish: redisData"+redisData);
        }
    }


    @Override
    public void loadInitKlineDate(String cacheName, String prodCode, byte period, List<PushTimeBar> initBars) {
        if(moreLevelEnable){
            IKlineDataSourceCache klineDataSourceEhCache = getDataSourceCache(prodCode,period);
            klineDataSourceEhCache.loadInitKlineDate(cacheName,prodCode,period,initBars);
        }
    }



    @Override
    public List<PushTimeBar> listKlineDate(String cacheName, String prodCode, byte period) {
        if(moreLevelEnable){
            IKlineDataSourceCache klineDataSourceEhCache = getDataSourceCache(prodCode,period);
            return klineDataSourceEhCache.listKlineDate(cacheName,prodCode,period);
        }else {
           return   klineDataSourceEhCacheImpl.listKlineDate(cacheName,prodCode,period);
        }
    }

    @Override
    public boolean removeKlineDate(String cacheName, String prodCode, byte period) {
        return false;
    }

    @Override
    public List<PushTimeBar> addLastKlineDate(PushTimeBar lastBar) {
        return null;
    }

    @Override
    public int getIndexByKlineDate(List<PushTimeBar> pbs, long klineDate) {
        return 0;
    }

    @Override
    public Float[] kline_field_range(ScriptInvokDataSourceParam sidsp) {
        return new Float[0];
    }

    @Override
    public List<PushTimeBar> kline_startdate_count(ScriptInvokDataSourceParam sidsp) {
        return null;
    }


    private IKlineDataSourceCache getDataSourceCache(String prodCode,byte period){
        String key=prodCode.substring(prodCode.lastIndexOf(".")+1)+":"+period;
        if(redisDataSet.contains(key)){
            return dataSourceMap.get("redisCache");
        }
        return dataSourceMap.get("defaultCache");
    }




}
