1.springboot+mybatis 配置3个mysql数据库，3个mysql数据库的表结构一样，希望根据业务能动态选择调用哪个数据。

在 Spring Boot 中结合 MyBatis 配置多个 MySQL 数据库，并根据业务动态选择调用其中一个数据库，可以通过配置多个数据源和动态数据源路由来实现。下面是一种实现方式的简要说明：

1.配置多个数据源：
首先在 applicaiton.properties 或 application.yml 中配置多个数据源的连接信息，例如：

spring.datasource.datasource1.url=jdbc:mysql://localhost:3306/db1
spring.datasource.datasource1.username=user1
spring.datasource.datasource1.password=password1

spring.datasource.datasource2.url=jdbc:mysql://localhost:3306/db2
spring.datasource.datasource2.username=user2
spring.datasource.datasource2.password=password2

spring.datasource.datasource3.url=jdbc:mysql://localhost:3306/db3
spring.datasource.datasource3.username=user3
spring.datasource.datasource3.password=password3

2.配置动态数据源路由：
创建一个动态数据源路由类，根据业务动态选择数据源。这个类可以继承 AbstractRoutingDataSource，重写 determineCurrentLookupKey() 方法来动态返回要使用的数据源的 key。

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}
3.数据源上下文持有类：
创建一个数据源上下文持有类 DataSourceContextHolder，用于设置和获取当前线程使用的数据源 key。

public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

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
4.配置 MyBatis：
配置 MyBatis 的 SqlSessionFactory 和 MapperScannerConfigurer，并将上述动态数据源路由类配置为数据源。

@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dynamicDataSource());
    return sqlSessionFactoryBean.getObject();
}

@Bean
public DataSource dynamicDataSource() {
    DynamicDataSource dynamicDataSource = new DynamicDataSource();
    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put("datasource1", dataSource1());
    dataSourceMap.put("datasource2", dataSource2());
    dataSourceMap.put("datasource3", dataSource3());
    dynamicDataSource.setTargetDataSources(dataSourceMap);
    dynamicDataSource.setDefaultTargetDataSource(dataSource1());
    return dynamicDataSource;
}

@Bean
@ConfigurationProperties("spring.datasource.datasource1")
public DataSource dataSource1() {
    return DataSourceBuilder.create().build();
}

@Bean
@ConfigurationProperties("spring.datasource.datasource2")
public DataSource dataSource2() {
    return DataSourceBuilder.create().build();
}

@Bean
@ConfigurationProperties("spring.datasource.datasource3")
public DataSource dataSource3() {
    return DataSourceBuilder.create().build();
}

5.动态切换数据源：
在需要切换数据源的地方，通过 DataSourceContextHolder.setDataSourceKey("datasourceX") 来设置要使用的数据源 key，然后执行相应的业务逻辑。
// 清除数据源 key（可选）
DataSourceContextHolder.clearDataSourceKey();

通过以上步骤，你可以在 Spring Boot 中配置多个 MySQL 数据源，并根据业务动态选择调用其中一个数据源来进行操作。需要根据具体业务逻辑来确定何时切换数据源以及使用哪个数据源。