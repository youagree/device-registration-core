
package ru.unit.techno.device.registration.core.impl.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Objects;

@Slf4j
@IntegrationTest
public class BaseTestClass {

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeTestClass
    private void init() {
        Resource resource = new ClassPathResource("init-test.sql");
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
        resourceDatabasePopulator.execute(Objects.requireNonNull(jdbcTemplate.getDataSource()));
        log.info("Start data uploaded!");
    }
}