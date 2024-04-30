package me.luoxiao.transformer.support.testing;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.luoxiao.transformer.support.testing.helpers.Resources;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * assume you:
 * using json content type to communicate between client and server.
 * your goals are simple, send http mock request, check the result, don't want to deal with boilerplate like mvc, perform and Matchers static methods etc...
 * <p>
 * extends from this class and do springboot integration test would be your choice.
 * <p>
 * I coded some practice I think is a convenient like
 * {@link me.luoxiao.transformer.support.testing.BaseIntegrationTest#getObjectMapper} for you to provide your own object mapper, if you want to reuse this class, and change it's behavior.
 * {@link me.luoxiao.transformer.support.testing.BaseIntegrationTest#pathPrefix} is what you must override to provide handler path prefix.
 *
 */
@SuppressWarnings({"unused", "SameParameterValue"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Slf4j
public abstract class BaseIntegrationTest extends BaseTest {
    @Autowired
    protected MockMvc mvc;

    public final static ObjectMapper mvcParamObjectMapper = new ObjectMapper();

    @JsonIgnoreType
    public static class MultipartFileMixin {
    }

    static {
        mvcParamObjectMapper.addMixIn(MultipartFile.class, MultipartFileMixin.class);
        mvcParamObjectMapper.registerModule(new JavaTimeModule());
    }

    protected static ResultHandler printHeader() {
        return result -> {
            for (String headerName : result.getResponse().getHeaderNames()) {
                log.info("{}={}", headerName, result.getResponse().getHeader(headerName));
            }
        };
    }

    protected static <S> Consumer<Object> propertiesEqual(final S expected) {
        return actual -> {
            if (expected == null) {
                assertThat(actual).isNull();
                return;
            }
            Field[] fields = expected.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object expectedValue = field.get(expected);
                    if (expectedValue == null) {
                        continue;
                    }
                    Field actualField = actual.getClass().getDeclaredField(field.getName());
                    actualField.setAccessible(true);
                    Object actualValue = actualField.get(actual);
                    assertThat(expectedValue).isEqualTo(actualValue);
                } catch (NoSuchFieldException e) {
                    throw new AssertionError("Field '" + field.getName() + "' does not exist in actual object", e);
                } catch (IllegalAccessException e) {
                    throw new AssertionError("Access problem with field '" + field.getName() + "'", e);
                }
            }
        };
    }

    protected static ResultHandler printSetCookieHeader(Consumer<Cookie[]> c) {
        return result -> {
            Cookie[] cookies = result.getResponse().getCookies();
            log.info("{}", (Object) cookies);
            if (c != null) {
                c.accept(cookies);
            }
        };
    }

    protected static ResultHandler printSetCookieHeader() {
        return printSetCookieHeader(null);
    }

    protected static ResultHandler saveBody(String pathOfSavedFile) {
        if (pathOfSavedFile.startsWith("/")) {
            throw new RuntimeException("no absolute path for testing!");
        }
        return result -> Files.write(
                Path.of(pathOfSavedFile),
                result.getResponse().getContentAsByteArray()
        );
    }

    protected static ResultHandler waitStreamingEnd(int timeoutSeconds) {
        return result -> Awaitility.await()
                .atMost(timeoutSeconds, TimeUnit.SECONDS)
                .until(() -> {
                    int contentLength = result.getResponse().getContentLength();
                    int received = result.getResponse().getContentAsByteArray().length;
                    log.info("contentLength={}, received={}", contentLength, received);
                    return contentLength > 0 && contentLength == received;

                });
    }

    protected static ResultHandler waitStreamingEnd() {
        return waitStreamingEnd(5);
    }

    protected static ResultHandler printBody() {
        return result -> log.info(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    protected static ResultHandler prettyPrintJsonBody() {
        return result -> log.info("\n{}",
                mvcParamObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        mvcParamObjectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8))
                )
        );
    }

    protected static StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

    protected static ResultMatcher requestAttribute(String name, Object value) {
        return MockMvcResultMatchers.request()
                .attribute(name, value);
    }

    protected static ResultMatcher requestSessionAttribute(String name, Object value) {
        return MockMvcResultMatchers.request()
                .sessionAttribute(name, value);
    }

    protected static HandlerResultMatchers handler() {
        return MockMvcResultMatchers.handler();
    }

    protected static ModelResultMatchers model() {
        return MockMvcResultMatchers.model();
    }

    protected static ViewResultMatchers view() {
        return MockMvcResultMatchers.view();
    }

    protected static ResultMatcher forwardedUrl(String url) {
        return MockMvcResultMatchers.forwardedUrl(url);
    }

    protected static ResultMatcher redirectedUrl(String url) {
        return MockMvcResultMatchers.redirectedUrl(url);
    }

    @SuppressWarnings("deprecation")
    protected static ResultMatcher loginSuccess(String redirectedUrl) {
        return ResultMatcher.matchAll(
                MockMvcResultMatchers.redirectedUrl(redirectedUrl),
                MockMvcResultMatchers.status().isFound()
        );
    }

    protected static ResultMatcher loginSuccess() {
        return loginSuccess("/");
    }

    protected static HeaderResultMatchers header() {
        return MockMvcResultMatchers.header();
    }

    protected static ContentResultMatchers content() {
        return MockMvcResultMatchers.content();
    }

    protected static <T> ResultMatcher code(int code) {
        return MockMvcResultMatchers.jsonPath("$.code", Matchers.is(code));
    }

    protected static <T> ResultMatcher code() {
        return code(0);
    }

    protected static <T> ResultMatcher codeSuccess() {
        return code(0);
    }

    protected static <T> ResultMatcher data(Object data) {
        return MockMvcResultMatchers.jsonPath("$.data", Matchers.is(data));
    }

    protected static <T> ResultMatcher pageDataLength(int length) {
        return MockMvcResultMatchers.jsonPath("$.data.data.length()", Matchers.is(length));
    }

    protected static <T> ResultMatcher message(String message) {
        return MockMvcResultMatchers.jsonPath("$.message", Matchers.is(message));
    }

    protected static <T> ResultMatcher messageContains(String message) {
        return MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString(message));
    }

    protected static CookieResultMatchers cookie() {
        return MockMvcResultMatchers.cookie();
    }

    protected static ResultMatcher sessionNotNull() {
        return result -> assertThat(result.getRequest().getSession(false))
                .isNotInstanceOf(MockHttpSession.class);
    }

    protected static ResultMatcher sessionIsNull() {
        return result -> assertThat(result.getRequest().getSession(false))
                .isInstanceOf(MockHttpSession.class);
    }

    protected MockRequestBuilder GET(String url, Object... urlPathVariables) {
        return new MockRequestBuilder(
                MockMvcRequestBuilders.get(pathPrefix() + url, urlPathVariables)
        );
    }

    protected MockRequestBuilder GET() {
        return GET("");
    }

    protected MockRequestBuilder POST(String url, Object... urlPathVariables) {
        MockRequestBuilder builder = new MockRequestBuilder(
                MockMvcRequestBuilders.post(pathPrefix() + url, urlPathVariables)
        );
        builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return builder;
    }

    protected MockRequestBuilder POST() {
        return POST("");
    }

    protected MockRequestBuilder MULTIPART(String url, Object... urlPathVariables) {
        return new MockRequestBuilder(
                MockMvcRequestBuilders.multipart(pathPrefix() + url, urlPathVariables)
        );
    }

    protected MockRequestBuilder MULTIPART() {
        return MULTIPART("");
    }

    protected MockRequestBuilder PUT(String url, Object... urlPathVariables) {
        MockRequestBuilder builder = new MockRequestBuilder(
                MockMvcRequestBuilders.put(pathPrefix() + url, urlPathVariables)
        );
        builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return builder;
    }

    protected MockRequestBuilder PUT() {
        return PUT("");
    }

    protected MockRequestBuilder DELETE(String url, Object... urlPathVariables) {
        MockRequestBuilder builder = new MockRequestBuilder(
                MockMvcRequestBuilders.delete(pathPrefix() + url, urlPathVariables)
        );
        builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return builder;
    }

    protected MockRequestBuilder DELETE() {
        return DELETE("");
    }

    @SuppressWarnings({"UnusedReturnValue", "RedundantThrows"})
    public class MockRequestBuilder implements ResultActions {
        private final MockHttpServletRequestBuilder requestBuilder;
        private boolean isContentTypeExplicitlySet = false;

        public MockRequestBuilder(MockHttpServletRequestBuilder requestBuilder) {
            this.requestBuilder = requestBuilder;
        }

        public MockRequestBuilder contentType(MediaType mediaType) {
            requestBuilder.contentType(mediaType);
            isContentTypeExplicitlySet = true;
            return this;
        }

        public MockRequestBuilder contentType(String mediaTypeString) {
            requestBuilder.contentType(mediaTypeString);
            isContentTypeExplicitlySet = true;
            return this;
        }

        public MockRequestBuilder content(String body) {
            requestBuilder.content(body);
            return this;
        }

        public MockRequestBuilder content(byte[] body) {
            requestBuilder.content(body);
            return this;
        }

        public MockRequestBuilder content(Object body) throws JsonProcessingException {
            requestBuilder.content(getObjectMapper().writeValueAsString(body));
            return this;
        }

        public MockRequestBuilder body(String body) throws JsonProcessingException {
            return content(body);
        }

        public MockRequestBuilder body(byte[] body) throws JsonProcessingException {
            return content(body);
        }

        public MockRequestBuilder body(Object body) throws JsonProcessingException {
            return content(body);
        }

        public MockRequestBuilder params(Object object) throws Exception {
            MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            mvm.setAll(getObjectMapper().convertValue(object, new TypeReference<>() {
            }));
            requestBuilder.params(mvm);
            if (requestBuilder instanceof MockMultipartHttpServletRequestBuilder mb) {
                Class<?> clazz = object.getClass();
                for (Field field : clazz.getDeclaredFields()) {
                    if (MultipartFile.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        Object mayBeMock = field.get(object);
                        if (!(mayBeMock instanceof MockMultipartFile)) {
                            if (mayBeMock != null) {
                                log.info("field={}, only support MockMultipartFile", field.getName());
                            }
                            continue;
                        }
                        mb.file((MockMultipartFile) mayBeMock);
                    }
                }
            }
            return this;
        }

        public MockRequestBuilder param(String name, String... values) {
            requestBuilder.param(name, values);
            return this;
        }

        public MockRequestBuilder file(String filename, String classPath) throws Exception {
            return file(filename, Resources.ofClassPath(classPath));
        }

        public MockRequestBuilder file(String filename, Resource resource) throws Exception {
            return file(filename, resource.getInputStream());
        }

        public MockRequestBuilder file(String filename, File file) throws Exception {
            return file(filename, new FileInputStream(file));
        }

        public MockRequestBuilder file(String filename, InputStream is) throws Exception {
            MockMultipartFile mockMultipartFile = new MockMultipartFile(filename, is);
            MockMultipartHttpServletRequestBuilder mb = (MockMultipartHttpServletRequestBuilder) requestBuilder;
            mb.file(mockMultipartFile);
            return this;
        }

        public MockRequestBuilder file(MockMultipartFile multipartFile) throws Exception {
            MockMultipartHttpServletRequestBuilder mb = (MockMultipartHttpServletRequestBuilder) requestBuilder;
            mb.file(multipartFile);
            return this;
        }

        public MockRequestBuilder with(RequestPostProcessor postProcessor) throws Exception {
            requestBuilder.with(postProcessor);
            return this;
        }

        public MockRequestBuilder header(String name, Object... values) {
            requestBuilder.header(name, values);
            return this;
        }

        public MockRequestBuilder bearer(String token) {
            assert !ObjectUtils.isEmpty(token);
            requestBuilder.header(HttpHeaders.AUTHORIZATION, "bearer " + token);
            return this;
        }

        public MockRequestBuilder headers(HttpHeaders headers) {
            requestBuilder.headers(headers);
            return this;
        }

        public MockRequestBuilder cookie(Cookie... cookies) {
            requestBuilder.cookie(cookies);
            return this;
        }

        public MockRequestBuilder part(String name, Object object) throws JsonProcessingException {
            MockMultipartHttpServletRequestBuilder mb = (MockMultipartHttpServletRequestBuilder) requestBuilder;
            MockMultipartFile part = new MockMultipartFile(
                    name,
                    null,
                    MediaType.APPLICATION_JSON.toString(),
                    getObjectMapper().writeValueAsString(object).getBytes());
            mb.file(part);
            return this;
        }

        private MockHttpServletRequestBuilder build() {
            return requestBuilder;
        }

        public ResultActions perform() throws Exception {
            return mvc.perform(build());
        }

        @Override
        public @NotNull ResultActions andExpect(@NotNull ResultMatcher matcher) throws Exception {
            return perform().andExpect(matcher);
        }

        @Override
        public @NotNull ResultActions andDo(@NotNull ResultHandler handler) throws Exception {
            return perform().andDo(handler);
        }

        @SneakyThrows
        @Override
        public @NotNull MvcResult andReturn() {
            return perform().andReturn();
        }
    }

    protected String pathPrefix() {
        return "";
    }

    protected ObjectMapper getObjectMapper() {
        return mvcParamObjectMapper;
    }
}
