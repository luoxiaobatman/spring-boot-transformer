package me.luoxiao.transformer.solution.telemetry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;

@Slf4j
public class TelemetryEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String isTelemetryEnabled = environment.getProperty(TelemetryProperties.PREFIX + ".enabled");
        String property = environment.getProperty("logging.pattern.level");

        if ("true".equals(isTelemetryEnabled) && property == null) {
            log.info("add telemetry logging support by modify logging.pattern.level property");
            HashMap<String, Object> map = new HashMap<>();
            map.put("logging.pattern.level", "%5p%replace( %X{trace_id} %X{span_id}){'  ', ''}");
            environment.getPropertySources().addLast(
                    new MapPropertySource("telemetryPropertySource", map)
            );
        }
    }
}
