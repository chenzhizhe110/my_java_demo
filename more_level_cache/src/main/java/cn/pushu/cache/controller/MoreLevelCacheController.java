package cn.pushu.cache.controller;

import cn.pushu.cache.entity.PushTimeBar;
import cn.pushu.cache.service.KlineMoreLevelCacheImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> MoreLevelCacheContraller<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/2614:32
 */
@RestController
public class MoreLevelCacheController {

    @Resource
    private  KlineMoreLevelCacheImpl klineMoreLevelCacheImpl;

    @GetMapping("int_kline_date")
    public String  initKlineDate(String prodCode,byte period,int count){
        List<PushTimeBar> timeBars=new ArrayList<PushTimeBar>();
        for(int i=0;i<=count;i++){
            PushTimeBar timeBar=new PushTimeBar(prodCode,period,i,i,  i,i,i,i);

            timeBars.add(timeBar);
        }
        klineMoreLevelCacheImpl.loadInitKlineDate(null,prodCode,period,timeBars);
        return  prodCode+":"+period+" ";
    }

    @GetMapping("list_kline_date")
    public List<PushTimeBar> listKlineDate(String prodCode, byte period) {
       return klineMoreLevelCacheImpl.listKlineDate(null,prodCode,period);
    }

}
