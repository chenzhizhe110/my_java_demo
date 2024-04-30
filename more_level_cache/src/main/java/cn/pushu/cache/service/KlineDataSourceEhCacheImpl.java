package cn.pushu.cache.service;
import cn.pushu.cache.config.CacheName;
import cn.pushu.cache.config.KlineServiceConf;
import cn.pushu.cache.entity.PushTimeBar;
import cn.pushu.cache.entity.ScriptInvokDataSourceParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> KlineDataSourceEhCacheImpl<br>
 * <b>projectName:</b> pushu_2022<br>
 * <b>description:</b> k线数据的缓存的ehcache的实现<br>
 * <b>date:</b> 2022/7/2216:25
 */
@Component
public class KlineDataSourceEhCacheImpl implements IKlineDataSourceCache {

    @Autowired
    private CacheManager cacheManager;

    @Resource
    private KlineServiceConf ksConf;
     private final Logger log= LoggerFactory.getLogger(KlineDataSourceEhCacheImpl.class);

    @Override
    public List<PushTimeBar> listKlineDate(String cacheName, String prodCode, byte period) {
        if(cacheName==null){
            cacheName= CacheName.KLINE_LONG;
        }
        return  getPushTimeBarFromCache(cacheName,prodCode,period);
    }

    @Override
    public boolean removeKlineDate(String cacheName, String prodCode, byte period) {
        if(cacheName==null){
            cacheName= CacheName.KLINE_LONG;
        }
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            return cache.evictIfPresent(prodCode + "#" + period);
        }
        return false;
    }

    @Override
    public void loadInitKlineDate(String cacheName, String prodCode, byte period, List<PushTimeBar> initBars) {
        if(cacheName==null){
            cacheName= CacheName.KLINE_LONG;
        }
        putCacheNKlineData(cacheName,prodCode,period,initBars);
    }

    /**
     * <p>
       description: 添加最新的合约的行情到缓存，线程不安全，通过调用者
                         来保证线程安全。<br>
     　 author chenzhizhe<br>
     　 date 2022/12/12 10:48 <br>
     *<p/>
     　* @param lastBar
     　 @return java.util.List<cn.pushu.marketpushcommon.entity.PushTimeBar>
    　*/
    @Override
    public  List<PushTimeBar> addLastKlineDate(PushTimeBar lastBar) {
        if(lastBar!=null) {
            try {
                String  prodCode= lastBar.getProdCode();
                byte  period= lastBar.getPeriod();
                List<PushTimeBar> pushTimeBarFromCache = getPushTimeBarFromCache(CacheName.KLINE_LONG,
                        prodCode, period);
                if (pushTimeBarFromCache != null) {
                        int lastIndex=pushTimeBarFromCache.size() - 1;
                        PushTimeBar timeBar = pushTimeBarFromCache.get(lastIndex);
                        if(timeBar.getPeriod()==lastBar.getPeriod()&&timeBar.getMinTime()==lastBar.getMinTime()&&
                                timeBar.getProdCode().equals(lastBar.getProdCode())){
                            //表示有重复
                            pushTimeBarFromCache.remove(lastIndex);
                        }
                        pushTimeBarFromCache.add(lastBar);
                        if (pushTimeBarFromCache.size() > ksConf.getMaxCount()) {
                            pushTimeBarFromCache.remove(0);
                        }
                    putCacheNKlineData(CacheName.KLINE_LONG,prodCode,period,pushTimeBarFromCache);
                    return  pushTimeBarFromCache;
                }
            } catch (Exception e) {
                log.error("addLastKlineDate exception:"+ e.getMessage());
            }
        }
        return  null;
    }

    @Override
    public int getIndexByKlineDate(List<PushTimeBar> bars, long klineDate) {
        return  Collections.binarySearch(bars, klineDate);
    }

    @Override
    public Float[] kline_field_range(ScriptInvokDataSourceParam sidsp) {
        String  prodCode=sidsp.getProdCode();
        byte period=sidsp.getPeriod();
        String startDate=sidsp.getStartDate();
        String endDate=sidsp.getEndDate();
        String fieldName=sidsp.getFieldName();

        List<PushTimeBar> cacheSticks =getPushTimeBarFromCache(CacheName.KLINE_LONG,prodCode,period);
        if(cacheSticks!=null&&cacheSticks.size()>0) {
            List<PushTimeBar> result = subStratEndCache(startDate, endDate, cacheSticks);
            if(result!=null) {
                return result.stream().map(k->k.getFieldValue(fieldName)).toArray(Float[]::new );
            }else {
                //如果为null,上层调用还会发起远程请求。此时缓存虽然有数据，
                // 但是没有命中所以还要从数据请求一次
                return null;
            }
        }
        //表示系统本身就没有这个合约的数据
        //所以不用在请求了。
        return new Float[0];
    }


    /**
     *
     * 从指定的startDate(开始时间)向前找count(需要计算的数量)根数据的k线，如果不足count则从从指定的startDate
     * 向前有多少取多少。该方法主要提供给选股服务使用。
     * @param sidsp   脚本处理器数据源参数
     * @return  当程序返回 null 上层调用还用通过远程接口调用去请求数据。
     *          如果为 new ArrayList<>() 则不会去请求。
     *          请特别注意 subList 的开始索引是从0 开始 ,结束索引为size,而不是size-1
     */
    @Override
    public List<PushTimeBar> kline_startdate_count(ScriptInvokDataSourceParam sidsp) {
        String  prodCode=sidsp.getProdCode();
        byte period=sidsp.getPeriod();
        int count=sidsp.getCount();
        List<PushTimeBar> nkCache = getPushTimeBarFromCache(CacheName.KLINE_LONG,prodCode,period);
        if(nkCache!=null) {
            long inStart;
            int size=nkCache.size();
            PushTimeBar lastKline = nkCache.get(size-1);
            //如果不传时间则表示取最新的数据
            if(sidsp.getStartDate()==null){
                inStart=lastKline.getMinTime();
                //如果是日内的小周期只有yyyymmdd 则将时间生成为当天的收盘时间
            }else if(sidsp.getStartDate()!=null&&period<6&&sidsp.getStartDate().length()==8){
                inStart=Long.parseLong(sidsp.getStartDate()+"1500");
            }else{
                assert sidsp.getStartDate() != null;
                inStart=Long.parseLong(sidsp.getStartDate());
            }
            //表示取当天或者一个未来的时间 总之就是取当前一个当前最新时间进行处理。
            if(lastKline.getMinTime()<=inStart) {
                int startIndex= Math.max(size - count, 0);
                return nkCache.subList(startIndex, size);
            }else {
                //取历史数据进行处理
                //因为subList的结束点为size 所以要查找的索引下标的基础上加1
                int binarySearch = Collections.binarySearch(nkCache, inStart)+1;
                if(binarySearch>0&&binarySearch-count>=0) {
                    return nkCache.subList(binarySearch - count, binarySearch);
                }else {
                    long firstCacheDate=nkCache.get(0).getMinTime();
                    //请求的时间超过缓存的最早时间，此时需要通过数据库去重新加载。
                    if(inStart<firstCacheDate) {
                        return null;
                    }else {
                        //如果是没有交易日则不返回数据，因为对应的基本面也没有数据
                        //第二种情况还是要处理一下，就是输入的inStart不是小时的整点数比如说
                        //1200就没有对应的k线记录，所以就要找到1200以前的k线1130的索引。
                        int endIndex = getEndIndex(inStart, nkCache)+1;
                        if(endIndex>0) {
                            int startIndex= Math.max(endIndex - count, 0);
                            return nkCache.subList(startIndex, endIndex);
                        }
                        //此时不会在去远程请求
                        return new ArrayList<>();
                    }
                }
            }
        }
        return new ArrayList<>();
    }


    private void putCacheNKlineData(String cacheName, String prodCode, int period, List<PushTimeBar> ptbs) {
        if(ptbs!=null&&ptbs.size()>0){
            Cache cache = cacheManager.getCache(cacheName);
            assert cache != null;
            cache.put(prodCode + "#" + period, ptbs);
        }
    }

    private List<PushTimeBar> getPushTimeBarFromCache(String cacheName, String prodCode, byte period) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(prodCode + "#" + period);
            if (valueWrapper != null) {
                Object objectValue = valueWrapper.get();
                return (List<PushTimeBar>) objectValue;
            }
        }
        return null;
    }

    /**
     * 从缓存中获取指定开始时间与结束时间之间的k线
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return List<PushTimeBar>
     */
    private List<PushTimeBar>  subStratEndCache(String startDate, String endDate,List<PushTimeBar> cacheKline){

        long inStart=Long.parseLong(startDate);
        long inEnd=Long.parseLong(endDate);

        long firstCacheDate=cacheKline.get(0).getMinTime();
        long endCacheDate=cacheKline.get(cacheKline.size()-1).getMinTime();
        //表示取行情的数据是很久以前，在缓存中没有需要去远程数据库查询，基实应该进行权限验证
        //只有后台管理员才有权限，查询更长时间的数据
        if(inStart<firstCacheDate) {
            return null;
        }
        //表示数据不足，可能最近有没有交易的股票
        if(firstCacheDate<inStart && endCacheDate< inEnd){
            //inEnd大于==当前时间，也就是说inEnd取了一个未来了时间
            if(endDate.length()>8){
                endDate=endDate.substring(0,8);
            }
            if(nowDayUntil(endDate)>=0) {
                inEnd=endCacheDate;
            }else {
                return new ArrayList<>();
            }
        }
        int startIndex=   Collections.binarySearch(cacheKline, inStart);
        if(startIndex<0) {
            return subCacheRange(inStart, inEnd, cacheKline);
        }
        int endIndex=   Collections.binarySearch(cacheKline, inEnd);
        if(endIndex<0) {
            return subCacheRange(inStart, inEnd, cacheKline);
        }

        return cacheKline.subList(startIndex, endIndex+1);
    }
    /**
     * 从缓存中获取指定开始时间与结束时间之间的k线收盘价
     * @return  List<PushTimeBar>
     */
    private List<PushTimeBar>  subCacheRange(long instart,long inEnd,List<PushTimeBar> cacheKline){
            List<PushTimeBar>  result= new ArrayList<>();
        for (PushTimeBar st : cacheKline) {
            long nkDate = st.getMinTime();
            if (nkDate >= instart && nkDate <= inEnd) {
                result.add(st);
            }
        }
           return  result;
    }


    private int getEndIndex(long instart, List<PushTimeBar> cacheKline) {
        PushTimeBar mid = cacheKline.get(cacheKline.size() / 2);
        long midCacheDate = mid.getMinTime();
        if (instart < midCacheDate) {
            for (int i = 0, j = cacheKline.size(); i < j; i++) {
                PushTimeBar st = cacheKline.get(i);
                long nkDate = st.getMinTime();
                if (nkDate >= instart) {
                    return i;
                }
            }
        } else {
            for (int j = cacheKline.size(), i = j - 1; i >= 0; i--) {
                PushTimeBar st = cacheKline.get(i);
                long nkDate = st.getMinTime();
                if (nkDate <= instart) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * <p>
     description: 获取当前时间与指定时间的差 在当天之前返回负数，在当天之后返回正数<br>
     　 author chenzhizhe<br>
     　 date 2022/8/17 10:42 <br>
     *<p/>
     　* @param startTime  格式 yyyyMMdd
     　 @return long
    　*/
    public static long nowDayUntil(String startTime){
         DateTimeFormatter ymdFormat =  DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate endDate=LocalDate.parse(startTime,ymdFormat);
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }


    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}