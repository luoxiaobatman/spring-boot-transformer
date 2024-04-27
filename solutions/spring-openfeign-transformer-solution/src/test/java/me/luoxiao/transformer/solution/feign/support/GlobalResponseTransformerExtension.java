package me.luoxiao.transformer.solution.feign.support;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.WireMockConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class GlobalResponseTransformerExtension {
    @Bean
    WireMockConfigurationCustomizer optionsCustomizer() {
        return options -> {
            options.extensions(new ResponseTemplateTransformer(true));
        };
    }
}
