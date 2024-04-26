package cn.pushu.cache.config;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> CacheName<br>
 * <b>projectName:</b> pushu_2022<br>
 * <b>description:</b> 一个缓存名称的常量定义 <br>
 * <b>date:</b> 2022/7/2614:51
 */
public class CacheName {


    public  static  final String  KLINE_LONG="kline_long";
    /**
     * 临时缓存的K线 存在这里
     */
    public  static final String  KLINE_SHORT  = "kline_short";
    /**
     * k线的年周期缓存
     */
    public static final String KLINE_YEAR="kline_year_long";


    /**
     * 策略托管的任务缓存
     */
    public static final  String  CLUSTER_JOB="cluster_job";

}
