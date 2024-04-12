package cn.pushu.mutipledynamicdatasource.config;

import cn.pushu.mutipledynamicdatasource.service.DynamicDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> DbConfig<br>
 * <b>projectName:</b> myTest<br>
 * <b>description:</b> 配置 MyBatis 的 SqlSessionFactory 和 MapperScannerConfigurer，并将上述动态数据源路由类配置为数据源。 <br>
 * <b>date:</b> 2024/4/12 11:00
 */
@Configuration
public class DbConfig {


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap();
        dataSourceMap.put("datasource1", dataSource1());
        dataSourceMap.put("datasource2", dataSource2());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSource1());
        return dynamicDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.datasource1")
    public DataSource dataSource1() {
        DruidDataSource ds=new DruidDataSource();
        return ds;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.datasource2")
    public DataSource dataSource2() {
//        DataSource build = DataSourceBuilder.create().build();
//        return build;
        DruidDataSource ds=new DruidDataSource();
        return ds;

    }


}
