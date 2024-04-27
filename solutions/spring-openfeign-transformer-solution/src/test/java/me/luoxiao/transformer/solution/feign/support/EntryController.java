package me.luoxiao.transformer.solution.feign.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ServiceFoo serviceFoo;

    @RequestMapping("/entry")
    public Object entry(String intraSVC) {
        return switch (intraSVC) {
            case ServiceFoo.REQUEST_GET_HEADERS -> serviceFoo.getHeaders();
            case ServiceFoo.REQUEST_GET_COOKIES -> serviceFoo.getCookies();
            default -> throw new RuntimeException();
        };
    }
}
