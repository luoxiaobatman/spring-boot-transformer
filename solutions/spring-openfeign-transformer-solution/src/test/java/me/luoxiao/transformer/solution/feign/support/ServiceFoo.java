package me.luoxiao.transformer.solution.feign.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
    String REQUEST_FIX_DELAY_10S = "FIX_DELAY_10S";
    String REQUEST_FIX_DELAY_500MS = "FIX_DELAY_500MS";
    String REQUEST_FORM_URLENCODED = "FORM_URLENCODED";
    String REQUEST_FORM_URLENCODED_RECORD = "FORM_URLENCODED_RECORD";
    String REQUEST_FORM_URLENCODED_POJO = "FORM_URLENCODED_POJO";
    String REQUEST_MULTIPART_FILE = "MULTIPART_FILE";
    String REQUEST_ENCRYPT_PARAM = "ENCRYPT_PARAM";


    @RequestMapping(REQUEST_GET_HEADERS)
    Map<String, String> getHeaders();

    @RequestMapping(REQUEST_GET_COOKIES)
    Map<String, String> getCookies();

    @RequestMapping(REQUEST_FIX_DELAY_10S)
    String fixDelay10s();

    @RequestMapping(REQUEST_FIX_DELAY_500MS)
    String fixDelay500ms();

    @PostMapping(value = REQUEST_FORM_URLENCODED, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String formUrlencodedPassingMap(Map<String, String> toBeEncoded);

    @PostMapping(value = REQUEST_FORM_URLENCODED_RECORD, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String formUrlencodedPassingRecord(UrlencodedRecord record);
    record UrlencodedRecord(String name) {}

    @PostMapping(value = REQUEST_FORM_URLENCODED_POJO, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String formUrlencodedPassingPojo(UrlencodedPojo pojo);
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class UrlencodedPojo {
        private String name;
    }

    @PostMapping(value = REQUEST_MULTIPART_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String multipartFile(MultipartFile multipartFile);
}
