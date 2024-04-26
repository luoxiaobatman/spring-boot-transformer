package me.luoxiao.transformer.solution.feign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = FeignProperties.PREFIX)
@Data
public class FeignProperties {
    public static final String PREFIX = "me.luoxiao.transformer.solution.feign";
}
