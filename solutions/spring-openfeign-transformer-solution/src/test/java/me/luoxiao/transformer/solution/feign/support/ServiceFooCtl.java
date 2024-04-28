package me.luoxiao.transformer.solution.feign.support;

import jakarta.servlet.http.Cookie;
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
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes sra) {
            Cookie[] cookies = sra.getRequest().getCookies();
            Map<String, String> map = new HashMap<>();
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(), cookie.getValue());
            }
            return map;
        }
        throw new RuntimeException();
    }
}
