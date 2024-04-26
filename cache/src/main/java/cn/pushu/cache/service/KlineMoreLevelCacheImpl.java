package cn.pushu.cache.service;

import cn.pushu.cache.entity.PushTimeBar;
import cn.pushu.cache.entity.ScriptInvokDataSourceParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> KlineMorelevelCacheImpl<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> k线的多线缓存的实现,通过注入不同的CacheManager来实现不同的级存<br>
 * <b>date:</b> 2024/4/25 15:58
 */
@Service
public class KlineMoreLevelCacheImpl implements IKlineDataSourceCache {
    @Autowired
    @Qualifier("redisCacheManager")
    private CacheManager redisCacheManager;
    @Autowired
    private CacheManager defaultCacheManager;
    @Autowired
    private KlineDataSourceEhCacheImpl klineDataSourceEhCacheImpl;

    @Override
    public void loadInitKlineDate(String cacheName, String prodCode, byte period, List<PushTimeBar> initBars) {
        setCacheManager(prodCode);
        klineDataSourceEhCacheImpl.loadInitKlineDate(cacheName,prodCode,period,initBars);
    }



    @Override
    public List<PushTimeBar> listKlineDate(String cacheName, String prodCode, byte period) {
        setCacheManager(prodCode);
        return klineDataSourceEhCacheImpl.listKlineDate(cacheName,prodCode,period);
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


    private boolean isConfMarket(String prodCode){
        if(prodCode.endsWith(".O")||prodCode.endsWith(".N")){
          return true;
        }
        return false;
    }

    private void setCacheManager(String prodCode) {
        if(isConfMarket(prodCode)){
            klineDataSourceEhCacheImpl.setCacheManager(redisCacheManager);
        }else {
            klineDataSourceEhCacheImpl.setCacheManager(defaultCacheManager);
        }
    }

}
