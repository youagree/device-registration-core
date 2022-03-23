
package ru.unit.techno.device.registration.core.impl.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.unit.techno.device.registration.core.impl.repository.*;

@Slf4j
@IntegrationTest
public class BaseTestClass {

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected BarrierRepository barrierRepository;

    @Autowired
    protected GroupsRepository groupsRepository;

    @Autowired
    protected QrRepository qrRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected RfidDevicesRepository rfidDevicesRepository;

    private static final String DB_NAME = "unit_techno";
    public static String DB_URL = null;

    private static final PostgreSQLContainer postgresDB = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(DB_NAME)
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withClasspathResourceMapping("init.sql", "/docker-entrypoint-initdb.d/init.sql", BindMode.READ_ONLY);

    static {
        postgresDB.start();
        DB_URL = String.format("jdbc:postgresql://%s:%d/unit_techno?currentSchema=device_registration_service",
                postgresDB.getContainerIpAddress(),
                postgresDB.getFirstMappedPort());
    }

    @DynamicPropertySource
    static void dynamicSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> DB_URL);
    }

    @AfterEach
    public void destroy() {
        barrierRepository.deleteAll();
        rfidDevicesRepository.deleteAll();
        groupsRepository.deleteAll();
    }
}