package me.luoxiao.transformer.message.agnostic;

import me.luoxiao.transformer.support.testing.BaseIntegrationTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@EnableAutoConfiguration
@TestPropertySource(properties = {
        MessageAgnosticProperties.PREFIX + ".enabled=true"
})
class BaseMessageTest extends BaseIntegrationTest {

}
