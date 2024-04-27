package me.luoxiao.transformer.solution.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Authorization header
 */
public class AddAuthorizationHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String bearerToken = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            template.header(HttpHeaders.AUTHORIZATION, bearerToken);
        }
    }
}
