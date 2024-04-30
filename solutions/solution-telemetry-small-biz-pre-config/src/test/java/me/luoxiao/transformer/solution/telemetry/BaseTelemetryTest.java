package me.luoxiao.transformer.solution.telemetry;

import me.luoxiao.transformer.support.testing.BaseIntegrationTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        TelemetryProperties.PREFIX + ".enabled=true",
})
@EnableAutoConfiguration
public class BaseTelemetryTest extends BaseIntegrationTest {

}
