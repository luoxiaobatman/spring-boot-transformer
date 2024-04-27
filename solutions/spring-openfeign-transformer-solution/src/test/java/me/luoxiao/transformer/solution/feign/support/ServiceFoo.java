package me.luoxiao.transformer.solution.feign.support;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(
        name = ServiceFoo.SERVICE_NAME
//        configuration = FeignConfigLogFull.class,
//        fallback =
)
public interface ServiceFoo {
    String SERVICE_NAME = "a-service-name";

    String REQUEST_GET_HEADERS = "GET_HEADERS";
    String REQUEST_GET_COOKIES = "GET_COOKIES";


    @RequestMapping(REQUEST_GET_HEADERS)
    Map<String, String> getHeaders();

    @RequestMapping(REQUEST_GET_COOKIES)
    Map<String, String> getCookies();
}
