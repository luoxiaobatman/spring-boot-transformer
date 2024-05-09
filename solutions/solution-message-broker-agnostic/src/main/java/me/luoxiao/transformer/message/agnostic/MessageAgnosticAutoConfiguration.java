package me.luoxiao.transformer.message.agnostic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        MessageAgnosticProperties.class
})
@ConditionalOnProperty(prefix = MessageAgnosticProperties.PREFIX, name = "enabled", havingValue = "true")
@AutoConfiguration
@Slf4j
public class MessageAgnosticAutoConfiguration {
}
