package me.luoxiao.transformer.solution.feign.lb;

import lombok.extern.slf4j.Slf4j;
import me.luoxiao.transformer.solution.feign.FeignProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.context.ApplicationListener;

/**
 * BlockingLoadBalancerClient with fallback, very convenient for prototyping and testing
 */
@Slf4j
public class FallbackBlockingLoadBalancerClient extends BlockingLoadBalancerClient implements ApplicationListener<WebServerInitializedEvent> {
    private int actualPort = 0;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FeignProperties properties;

    public FallbackBlockingLoadBalancerClient(ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory) {
        super(loadBalancerClientFactory);
    }

    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        ServiceInstance instance = super.choose(serviceId, request);
        if (instance != null || properties.getStrategy() == Strategy.NOOP) {
            return instance;
        }
        DefaultServiceInstance fallbackInstance = new DefaultServiceInstance();
        fallbackInstance.setServiceId(serviceId);
        fallbackInstance.setHost(properties.getServiceInstanceFallbackHost());
        if (properties.getServiceInstanceFallbackPort() > 0) {
            fallbackInstance.setPort(properties.getServiceInstanceFallbackPort());
        } else {
            fallbackInstance.setPort(actualPort);
        }
        return fallbackInstance;
    }

    @Override
	public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
		int port = webServerInitializedEvent.getWebServer().getPort();
		if (port > 0) {
            actualPort = port;
		}
	}
}
