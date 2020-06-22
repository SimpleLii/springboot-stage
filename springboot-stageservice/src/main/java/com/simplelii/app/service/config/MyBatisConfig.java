package com.simplelii.app.service.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Configuration
@MapperScan(basePackages = "com.simplelii.app.dao.mapper")
public class MyBatisConfig {

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Value("${spring.datasource.setValidationQuery}")
    private String validationQuery;

    @Bean(name = "dataSource")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());

        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(false);

        dataSource.setTestOnReturn(false);

        dataSource.setTestWhileIdle(true);

        dataSource.setTimeBetweenEvictionRunsMillis(60000L);

        dataSource.setMinEvictableIdleTimeMillis(25200000L);

        dataSource.setRemoveAbandoned(true);

        dataSource.setRemoveAbandonedTimeout(1800);

        dataSource.setLogAbandoned(true);
        dataSource.setBreakAfterAcquireFailure(true);

        return dataSource;

    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        // 设置插件
        sqlSessionFactoryBean.setPlugins(createInterceptors());
        return sqlSessionFactoryBean.getObject();
    }

    private Interceptor[] createInterceptors() {
        Interceptor[] plugins = new Interceptor[1];
        // 分页插件
        plugins[0] = new PageInterceptor();
        return plugins;
    }


}
