package me.luoxiao.transformer.solution.feign.lb;

import me.luoxiao.transformer.solution.feign.BaseFeignTest;
import me.luoxiao.transformer.solution.feign.support.ServiceFoo;
import me.luoxiao.transformer.support.testing.annotation.meta.TestPropertiesLoggingDebug;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("RedundantThrows")
@TestPropertiesLoggingDebug
class FallbackBlockingLoadBalancerClientTest extends BaseFeignTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ServiceFoo client;

    @Test
    public void givenFeignClientNoServiceInstanceProvide_whenNoFallback_throwException() {
        assertThatThrownBy(() -> client.getHeaders())
                .isInstanceOf(NoFallbackAvailableException.class);
    }
}