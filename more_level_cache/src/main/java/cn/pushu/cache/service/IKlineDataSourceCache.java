package cn.pushu.cache.service;


import cn.pushu.cache.entity.PushTimeBar;
import cn.pushu.cache.entity.ScriptInvokDataSourceParam;

import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> IKlineDataSourceCache<br>
 * <b>projectName:</b> pushu_2022<br>
 * <b>description:</b> 一个k线数据的缓存接口 <br>
 * <b>date:</b> 2022/7/229:59
 */
public interface IKlineDataSourceCache {


    /**
     * <p>
         description: k线的初始化加载<br>
         author chenzhizhe<br>
         date 2022/8/15 10:53 <br>
     *<p/>
     * @param cacheName 如果为null 默认值为:CacheName.KLINE_LONG
     * @param prodCode 股票代码
     * @param period   周期
     * @param initBars  需要加载的最新行情
    　*/
      void  loadInitKlineDate(String cacheName,String  prodCode, byte period,List<PushTimeBar> initBars);


    /**
     * <p>
        description: 从缓存中获取k线数据<br>
     　 author chenzhizhe<br>
     　 date 2022/7/22 10:00 <br>
     *<p/>
     * @param prodCode  股票代码
     * @param period    周期
     * @param cacheName  缓存名称 如果为null 则默认为 kline_long
    　 @return java.util.List<cn.pushu.marketpushcommon.entity.PushTimeBar>
    　*/
     List<PushTimeBar> listKlineDate(String cacheName,String  prodCode, byte period);


    /**
     * <p>
       description: 从缓存中删除k线数据<br>
     　 author chenzhizhe<br>
     　 date 2022/7/22 10:00 <br>
     *<p/>
     * @param prodCode  股票代码
     * @param period    周期
     * @param cacheName  缓存名称 如果为null 则默认为 kline_long
    　 @return boolean
    　*/
     boolean removeKlineDate(String cacheName,String prodCode, byte period);

    /**
     * <p>
         description: 添加一新最新k线行情数据到缓存中(添加到队尾)
        主要用于将推送过来最新k线数据添加到缓存中<br>
         author chenzhizhe<br>
         date 2022/7/26 14:20 <br>
     *<p/>
     　 @param lastBar  最新的k线行情。 lastBar的 prodCode,Period 为必须的字段不能为null
     　 @return List<PushTimeBar>
    　*/
      List<PushTimeBar> addLastKlineDate(PushTimeBar lastBar);

    /**
     * <p>
        description: 根据k线时间获取它在缓存在索引<br>
     　 author chenzhizhe<br>
     　 date 2022/7/22 14:45 <br>
     *<p/>
     * @param pbs
     * @param klineDate  k线时间
    　 @return int
    　*/
      int  getIndexByKlineDate(List<PushTimeBar> pbs,long klineDate);

    /**
     * <p>
        description:  k线获取指定字段的时间范围内的数据 <br>
     　 author chenzhizhe<br>
     　 date 2022/8/15 10:57 <br>
     *<p/>
     　 * @param sidsp 必须填写的参数
         ProdCode;
         Period
         getStartDate;
         getEndDate;
         getFieldName();
         数据源名称 kline_field_range
     　 @return java.lang.Float[]
    　*/
      Float[]  kline_field_range(ScriptInvokDataSourceParam sidsp);


    /**
     * <p>
         description: 从缓存中获取指定时间(startDate)之前的指定数量的k线
         数据源名称   kline_startdate_count<br>
         author chenzhizhe<br>
     　  date 2022/8/15 11:03 <br>
     *<p/>
     　 @param sidsp
     　 @return  List<PushTimeBar>

    　*/
      List<PushTimeBar> kline_startdate_count(ScriptInvokDataSourceParam sidsp);



}
