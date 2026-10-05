package com.mbc.mtps;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * demo 프로필 전용 DB 스택 (내장 Postgres 5433 + 시드).
 * temp 디렉시 사용 → 재시작마다 fresh (데모 오염 자동 초기화).
 * 로컬 5432 Postgres와 충돌 없음. DatabaseConfig는 !demo에서만 동작.
 */
@Configuration
@Profile("demo")
@MapperScan(
    basePackages = "com.mbc.mtps.dao",
    sqlSessionTemplateRef = "sqlSessionTemplate")
public class DemoDbConfig {

    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        // 고아 프로세스가 잡고 있을 수 있어 빈 포트 탐색 (5433 우선)
        return EmbeddedPostgres.builder().setPort(freePort()).start();
    }

    private static int freePort() {
        for (int port : new int[] {5433, 5434, 5435, 5436, 5437}) {
            try (java.net.ServerSocket s = new java.net.ServerSocket(port)) {
                s.setReuseAddress(true);
                return port;
            } catch (IOException ignored) {
            }
        }
        try (java.net.ServerSocket s = new java.net.ServerSocket(0)) {
            return s.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("빈 포트를 찾지 못했습니다.", e);
        }
    }

    @Bean
    @DependsOn("embeddedPostgres")
    public DataSource dataSource(
            EmbeddedPostgres pg,
            @Value("${spring.datasource.hikari.username}") String username,
            @Value("${spring.datasource.hikari.password}") String password,
            @Value("${spring.datasource.hikari.driver-class-name}") String driver) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:" + pg.getPort() + "/postgres");
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration =
                new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:sqls/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
