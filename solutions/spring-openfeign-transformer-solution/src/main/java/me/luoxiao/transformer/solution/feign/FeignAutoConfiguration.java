package me.luoxiao.transformer.solution.feign;

import me.luoxiao.transformer.nodep.MessageSourceConfigure;
import me.luoxiao.transformer.solution.feign.interceptor.AddAuthorizationHeaderRequestInterceptor;
import me.luoxiao.transformer.solution.feign.interceptor.AddCookiesRequestInterceptor;
import me.luoxiao.transformer.solution.feign.interceptor.TelemetryRequestInterceptor;
import me.luoxiao.transformer.solution.feign.lb.FallbackBlockingLoadBalancerClient;
import me.luoxiao.transformer.solution.feign.properties.Distributed;
import me.luoxiao.transformer.solution.feign.properties.Localhost;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties({
        FeignProperties.class
})
@Import({
        Distributed.class,
        Localhost.class,
})
public class FeignAutoConfiguration implements MessageSourceConfigure {

    public static final String I18N_BASENAME = "i18n/me/luoxiao/transformer/solution/feign";
    public static final String BEAN_NAME_FALLBACK_BLOCKING_LOAD_BALANCER_CLIENT = "fallbackBlockingLoadBalancerClient";

    @Override
    public String i18nBasename() {
        return I18N_BASENAME;
    }

    @Bean(name = BEAN_NAME_FALLBACK_BLOCKING_LOAD_BALANCER_CLIENT)
    @ConditionalOnMissingBean(name = BEAN_NAME_FALLBACK_BLOCKING_LOAD_BALANCER_CLIENT)
    FallbackBlockingLoadBalancerClient fallbackBlockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FallbackBlockingLoadBalancerClient(loadBalancerClientFactory);
    }

    @Bean
    @ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "reject-authorize-header", havingValue = "false", matchIfMissing = true)
    public AddAuthorizationHeaderRequestInterceptor addAuthorizationHeaderRequestInterceptor() {
        return new AddAuthorizationHeaderRequestInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "reject-cookies", havingValue = "false", matchIfMissing = true)
    public AddCookiesRequestInterceptor addCookiesRequestInterceptor() {
        return new AddCookiesRequestInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "reject-telemetry", havingValue = "false", matchIfMissing = true)
    public TelemetryRequestInterceptor telemetryRequestInterceptor() {
        return new TelemetryRequestInterceptor();
    }
}
