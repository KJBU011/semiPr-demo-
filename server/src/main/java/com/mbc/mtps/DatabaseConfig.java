package com.mbc.mtps;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@org.springframework.context.annotation.Configuration
@org.springframework.context.annotation.Profile("!demo") // demo 프로필에서는 DemoDbConfig가 DB 스택 전담
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println("dataSource : " + dataSource);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception {
		System.out.println("DatabaseConfig sqlSessionFactory()");

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(datasource);

		// MyBatis 설정 (카멜케이스 자동 변환)
		Configuration mybatisConfiguration = new Configuration();
		mybatisConfiguration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);

		// Mapper XML 파일 경로 설정
		Resource[] arrResource = new PathMatchingResourcePatternResolver()
				.getResources("classpath:sqls/*.xml");
		sqlSessionFactoryBean.setMapperLocations(arrResource);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sf) {
		return new SqlSessionTemplate(sf);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}