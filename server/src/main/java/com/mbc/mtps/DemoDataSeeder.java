package com.mbc.mtps;

import javax.sql.DataSource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

/** demo 프로필: DDL + 시드 적재 (create_table → insert_data → demo-seed) */
@Component
@Profile("demo")
public class DemoDataSeeder implements ApplicationRunner {

    private final DataSource dataSource;

    public DemoDataSeeder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] files = {"demo/create_table.sql", "demo/insert_data.sql", "demo/demo-seed.sql"};
        for (String f : files) {
            try (var conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource(f));
                System.out.println("[demo-seed] loaded: " + f);
            }
        }
    }
}
