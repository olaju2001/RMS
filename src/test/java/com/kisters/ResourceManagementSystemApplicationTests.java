package com.kisters;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=sa"
})
class ResourceManagementSystemApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
    }
}
