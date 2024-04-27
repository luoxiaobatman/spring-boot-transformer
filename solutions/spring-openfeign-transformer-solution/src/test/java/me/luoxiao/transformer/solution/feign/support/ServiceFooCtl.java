package me.luoxiao.transformer.solution.feign.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceFooCtl implements ServiceFoo {

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes sra) {
            HttpServletRequest request = sra.getRequest();

            if (request.getHeaderNames().hasMoreElements()) {
                map.put(request.getHeaderNames().nextElement(), request.getHeader(request.getHeaderNames().nextElement()));
            }
        }
        return map;
    }


    @Override
    public Map<String, String> getCookies() {
        return Map.of("fallback", "fallback");
    }
}
