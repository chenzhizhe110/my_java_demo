package cn.pushu.cache.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> EhcacheWr<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> 一个测试ehcach读写的示例 <br>
 * <b>date:</b> 2024/4/1815:51
 */
@Service
public class EhcacheWr {

    @Autowired
    private CacheManager cacheManager;


    public String writeKline(int count){
        long size=0;
        Cache klineLong = cacheManager.getCache("kline_long");
        for(int i=0;i<=count;i++){
          String text="ehcache_"+i;
          size=size+text.getBytes().length;
          klineLong.put(i+"",text);
        }
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory()/1024/1024;
        long total = run.totalMemory()/1024/1024;
        long free = run.freeMemory()/1024/1024;
        long usable = max - total + free;

        System.out.println("最大内存 = " + max);
        System.out.println("已分配内存 = " + total);
        System.out.println("已分配内存中的剩余空间 = " + free);
        System.out.println("最大可用内存 = " + usable);

        return "添加的元素数量:"+count+" 添加的数据大小:"+size+" Byte"+
                "\n 最大内存:"+max+" 已分配内存:"+total+" 已分配内存中的剩余空间:"+free+" 最大可用内存:"+usable;
    }


    public String readKline(int count){
        Cache klineLong = cacheManager.getCache("kline_long");
        for(int i=0;i<=count;i++){
            Cache.ValueWrapper valueWrapper = klineLong.get(i + "");
            if(valueWrapper!=null){
                System.out.println(valueWrapper.get());
            }
        }
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory()/1024/1024;
        long total = run.totalMemory()/1024/1024;
        long free = run.freeMemory()/1024/1024;
        long usable = max - total + free;

        System.out.println("最大内存 = " + max);
        System.out.println("已分配内存 = " + total);
        System.out.println("已分配内存中的剩余空间 = " + free);
        System.out.println("最大可用内存 = " + usable);
        return count+"条数据读取完成\n 最大内存:"+max+" 已分配内存:"+total+" 已分配内存中的剩余空间:"+free+" 最大可用内存:"+usable;
    }
}
