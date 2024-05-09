package me.luoxiao.transformer.message.agnostic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = MessageAgnosticProperties.PREFIX)
@Data
public class MessageAgnosticProperties {
    public static final String PREFIX = "me.luoxiao.transformer.solution.message.agnostic";

    private boolean enabled = false;
}
