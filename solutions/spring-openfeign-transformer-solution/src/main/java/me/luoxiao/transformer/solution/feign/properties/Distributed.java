package me.luoxiao.transformer.solution.feign.properties;

import me.luoxiao.transformer.nodep.YamlPropertySourceFactory;
import me.luoxiao.transformer.solution.feign.FeignProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:me/luoxiao/transformer/solution/feign/distributed.yaml", factory = YamlPropertySourceFactory.class)
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "preset-properties", havingValue = "distributed")
public class Distributed {
}
