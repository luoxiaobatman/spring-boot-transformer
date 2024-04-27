package me.luoxiao.transformer.solution.feign.interceptor;

import me.luoxiao.transformer.solution.feign.BaseFeignTest;
import me.luoxiao.transformer.solution.feign.FeignProperties;
import me.luoxiao.transformer.solution.feign.support.ServiceFoo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource(properties = {
        FeignProperties.PREFIX + ".strategy=custom_server"
})
class AddAuthorizationHeaderRequestInterceptorTest extends BaseFeignTest {
    @Test
    public void passJwtTokenHeaderToRPCServiceOK() throws Exception {
        String bearerJwtToken = "bearer: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE3MDI4ODg2MDEsImV4cCI6MTcwMjk3NTAwMX0.yPZEL8okBBzZ6n3leI-0l7A8519eXQ_DPvMEgn3JyOM";
        entry(ServiceFoo.REQUEST_GET_HEADERS)
                .header(HttpHeaders.AUTHORIZATION, bearerJwtToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorization").value(
                        bearerJwtToken
                ))
                .andDo(printBody());
    }

    @Test
    public void whenHeaderNotPresent_rpcSVCCalledWithNoAuthHeader() throws Exception {
        entry(ServiceFoo.REQUEST_GET_HEADERS)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + HttpHeaders.AUTHORIZATION.toLowerCase()).doesNotExist())
                .andDo(printBody());
    }

}
