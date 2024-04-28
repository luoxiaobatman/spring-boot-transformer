package me.luoxiao.transformer.solution.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Add Cookies
 */
public class AddCookiesRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Cookie[] requestCookies = attributes.getRequest().getCookies();
            if (requestCookies != null) {
                for (Cookie cookie : requestCookies) {
                    Collection<String> cookies = template.headers().get(HttpHeaders.COOKIE);
                    if (cookies == null) {
                        cookies = new ArrayList<>();
                    } else {
                        cookies = new ArrayList<>(cookies);
                    }
                    cookies.add(new HttpCookie(cookie.getName(), cookie.getValue()).toString());
                    template.header(HttpHeaders.COOKIE, cookies);
                    return;
                }
            }
        }
    }
}
