package cn.pushu.mutipledynamicdatasource.service;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 创建一个动态数据源路由类，根据业务动态选择数据源。这个类可以继承 AbstractRoutingDataSource，
 * 重写 determineCurrentLookupKey() 方法来动态返回要使用的数据源的 key。
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}
