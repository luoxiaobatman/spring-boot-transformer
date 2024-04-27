package me.luoxiao.transformer.solution.feign;

import lombok.Data;
import me.luoxiao.transformer.solution.feign.lb.Strategy;
import me.luoxiao.transformer.solution.feign.properties.Preset;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = FeignProperties.PREFIX)
@Data
public class FeignProperties {
    public static final String PREFIX = "me.luoxiao.transformer.solution.feign";

    private boolean enabled;

    private Strategy strategy = Strategy.NOOP;

    private String serviceInstanceFallbackHost = "127.0.0.1";

    /**
     * 0: for auto discovery for same spring context app
     */
    private int serviceInstanceFallbackPort = 0;

    private Preset presetProperties;

    private boolean rejectAuthorizeHeader = false;
}
