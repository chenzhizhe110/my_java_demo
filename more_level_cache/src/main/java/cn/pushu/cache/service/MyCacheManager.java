package cn.pushu.cache.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> MyCacheManager<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> 自定义的缓存管理器 <br>
 * <b>date:</b> 2024/4/23 14:10
 */
//@Service
public class MyCacheManager implements CacheManager {

    @Override
    public Cache getCache(String name) {
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return null;
    }
}
