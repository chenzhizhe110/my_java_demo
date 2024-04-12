package cn.pushu.mutipledynamicdatasource.service;

/**
 * 创建一个数据源上下文持有类 DataSourceContextHolder，用于设置和获取当前线程使用的数据源 key。
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }
}
