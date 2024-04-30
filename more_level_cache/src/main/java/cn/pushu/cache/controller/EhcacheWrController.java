package cn.pushu.cache.controller;

import cn.pushu.cache.service.EhcacheWr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> EhcacheWrController<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/1816:28
 */
@RestController
public class EhcacheWrController {

    @Resource
    private EhcacheWr ehcacheWr;



    @GetMapping("/write_kline")
    public String writeKline(int count){
        return  ehcacheWr.writeKline(count);
    }

    @GetMapping("read_kline")
    public String readKline(int count){
        return  ehcacheWr.readKline(count);
    }




}
