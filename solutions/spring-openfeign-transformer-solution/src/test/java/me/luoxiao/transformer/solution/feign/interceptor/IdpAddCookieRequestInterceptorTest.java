package me.luoxiao.transformer.solution.feign.interceptor;

import jakarta.servlet.http.Cookie;
import me.luoxiao.transformer.solution.feign.BaseFeignTest;
import me.luoxiao.transformer.solution.feign.FeignProperties;
import me.luoxiao.transformer.solution.feign.support.ServiceFoo;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource(properties = {
        FeignProperties.PREFIX + ".reject-cookies=false",
})
class IdpAddCookieRequestInterceptorTest extends BaseFeignTest {
    @Test
    public void cookiePassOnToFeignClientOK() throws Exception {
        String expected = "expected";
        entry(ServiceFoo.REQUEST_GET_COOKIES)
                .cookie(new Cookie("some-cookie", expected))
                .andExpect(jsonPath("$.some-cookie").value(expected));
    }
}
