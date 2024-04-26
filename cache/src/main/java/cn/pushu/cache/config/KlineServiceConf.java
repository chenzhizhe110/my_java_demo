package cn.pushu.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> RibbonConf<br>
 * <b>projectName:</b> pushu_2022<br>
 * <b>description:</b> k线行情服务器的相关配置<br>
 * <b>date:</b> 2022/7/2215:18
 */
@Configuration
public class KlineServiceConf {


     //不同周期的请求数据的最早的开始时间，如果比这个时间还小，则超出取范围，给出报错提示
    private static final ConcurrentHashMap<String,Long>   periodMinStartTime=new ConcurrentHashMap<>();

    private String[] url;
    private int minCount;
    private int maxCount;
    //用于财智码实时计算的k线数量
    private int alertCalcCount=50;
    //特殊周期的最小数量
    private Map<Byte, Integer> minCountMap;
    //需要请求的行情周期
    private List<Byte> period;
    //  #缓存穿透默认为false,如果为true如果此时正在进行选股如果缓存中请求不到则会通过远程接口向服务器请求。
    private  boolean  enableCacheBreakdown;
    // #是否启用选股的行情监听
    private  boolean enableSelectStock;
    // 是否启用择时的行情监听
    private  boolean enableSelectTime;
    //线程模式 1 单线程模式 2 多线程模式  默认值为1
    //select_stock_20194 建议为多线程模式
    private  int threadModel=1;


    public List<String> getUrl() {
        List<String> urlList = new CopyOnWriteArrayList<>();
        Collections.addAll(urlList, url);
        return  urlList;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }

    /**
     * 用于获取策略计算的最小k线的根数。主要用于初始化的数据请求
     * @param period  周期
     * @return int
     */
    public int getMinCount(Byte period) {
        Integer  count = minCountMap.get(period);
        if(count==null){
            return minCount;
        }
        return  count;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {

        return maxCount;
    }
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setMinCountMap(Map<Byte, Integer> minCountMap) {
        this.minCountMap = minCountMap;
    }

    public List<Byte> getPeriod() {
        return period;
    }

    public void setPeriod(List<Byte> period) {
        this.period = period;
    }



    public boolean isEnableCacheBreakdown() {
        return enableCacheBreakdown;
    }

    public void setEnableCacheBreakdown(boolean enableCacheBreakdown) {
        this.enableCacheBreakdown = enableCacheBreakdown;
    }

    public boolean isEnableSelectStock() {
        return enableSelectStock;
    }

    public void setEnableSelectStock(boolean enableSelectStock) {
        this.enableSelectStock = enableSelectStock;
    }

    public boolean isEnableSelectTime() {
        return enableSelectTime;
    }

    public void setEnableSelectTime(boolean enableSelectTime) {
        this.enableSelectTime = enableSelectTime;
    }

    public int getThreadModel() {
        return threadModel;
    }

    public void setThreadModel(int threadModel) {
        this.threadModel = threadModel;
    }

    public int getAlertCalcCount() {
        return alertCalcCount;
    }

    public void setAlertCalcCount(int alertCalcCount) {
        this.alertCalcCount = alertCalcCount;
    }

    /**
     *
     * @param period  格式有两种  1 Period+"#"+"before"   //用于账智码表示指定的缓存中最早的开始时间
     *                          2 Period+"#"+"pcc"    //用于相似K线表示在缓存中最早的开始时间
     * @param startTime  开始时间
     */
    public  static  void  addAlertPeriodStartTime(byte period, long startTime){
        String key=period+"#before";
        Long oldStartTime = periodMinStartTime.get(key);
        //保存开始时间的最小值
        if(oldStartTime==null||startTime<oldStartTime){
            periodMinStartTime.put(key,startTime);
        }
    }

    public  static  void  addPPCPeriodStartTime(byte period, long startTime){
        String key=period+"#pcc";//相似k线
        Long oldStartTime = periodMinStartTime.get(key);
        //保存开始时间的最小值
        if(oldStartTime==null||startTime<oldStartTime){
            periodMinStartTime.put(key,startTime);
        }
    }

    public  static  Long  getAlertPeriodMinStartTime(byte period){
       return  periodMinStartTime.get(period+"#before");
    }

    public  static  Long  getPccPeriodMinStartTime(byte period){
        return  periodMinStartTime.get(period+"#pcc");
    }

    /**
     * true 表示财智码数据请求超过了缓存中最早的数据，此时将不提供运行服务
     * @param startTime 请求k线数据的开始时间
     * @return boolean
     */
    public  static  boolean isBeforeAlertOverStartTime(byte period,Long startTime){
        Long minStartTime = periodMinStartTime.get(period+"#before");
        return minStartTime == null || startTime < minStartTime;
    }

    /**
     * true 表示相似k数据请求超过了缓存中最早的数据，此时将不提供运行服务
     * @param startTime 请求k线数据的开始时间

     */
    public  static  boolean isBeforePccOverStartTime(byte period,Long startTime){
        Long minStartTime = periodMinStartTime.get(period+"#pcc");
        return minStartTime == null || startTime < minStartTime;
    }
}

