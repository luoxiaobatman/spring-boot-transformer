package me.luoxiao.transformer.solution.feign;


import me.luoxiao.transformer.solution.feign.support.EntryController;
import me.luoxiao.transformer.solution.feign.support.GlobalResponseTransformerExtension;
import me.luoxiao.transformer.solution.feign.support.ServiceFoo;
import me.luoxiao.transformer.solution.feign.support.ServiceFooCtl;
import me.luoxiao.transformer.support.testing.BaseIntegrationTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@EnableAutoConfiguration
@ContextConfiguration(classes = {
        ServiceFoo.class,
        GlobalResponseTransformerExtension.class,
        EntryController.class,
        ServiceFooCtl.class,
})
@TestPropertySource(properties = {
        FeignProperties.PREFIX + ".enabled=true",
//        "spring.cloud.discovery.client.simple.instances." + ServiceFoo.SERVICE_NAME + "[0].uri=http://127.0.0.1:${wiremock.server.port}",
        "spring.cloud.openfeign.autoconfiguration.jackson.enabled=true",
        "spring.cloud.loadbalancer.retry.enabled=true"
})
@AutoConfigureWireMock(port = 0)
@EnableFeignClients(clients = {
        ServiceFoo.class,
})
public class BaseFeignTest extends BaseIntegrationTest {
    @Override
    protected String pathPrefix() {
        return "";
    }

    protected MockRequestBuilder entry(String intraSVC) {
        return MULTIPART("/entry")
                .param("intraSVC", intraSVC);
    }
}
