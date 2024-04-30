package cn.pushu.cache.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Configuration
public class CustomScopeConfigurer implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 空实现
    }
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition beanDefinition = registry.getBeanDefinition("klineDataSourceEhCacheImpl");
        Boolean moreLevelEnable = environment.getProperty("moreLevel.enable", Boolean.class);
        // 根据条件动态设置 Bean 的作用域
        if (moreLevelEnable) {
            beanDefinition.setScope("prototype");
        } else {
            beanDefinition.setScope("singleton");
        }
    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}
