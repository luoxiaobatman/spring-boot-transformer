package me.luoxiao.transformer.support.testing;

import me.luoxiao.transformer.support.testing.annotation.meta.Testing;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * provide base class to be extended.
 * <p>
 * you can also use annotation {@link Testing}(and every meta annotations inside the save package) approach to do the boilerplate.
 */
@Testing
abstract public class BaseTest {
    protected static MockMultipartFile multipartFile() {
        return new MockMultipartFile("myMultipartFile", "myMultipartFile",
                MediaType.APPLICATION_JSON_VALUE, new byte[8]);
    }

    protected static MockMultipartFile multipartFile(String filename) {
        return new MockMultipartFile(filename, filename,
                MediaType.APPLICATION_JSON_VALUE, new byte[8]);
    }

    protected static MockMultipartFile multipartFile(String filename, String content) {
        return multipartFile(filename, content.getBytes(StandardCharsets.UTF_8));
    }

    protected static MockMultipartFile multipartFile(String filename, byte[] content) {
        return new MockMultipartFile(filename, filename,
                MediaType.APPLICATION_JSON_VALUE, content);
    }

    protected static MockMultipartFile multipartFile(int bytesSize) {
        return new MockMultipartFile("myMultipartFile", "myMultipartFile",
                MediaType.APPLICATION_JSON_VALUE, new byte[bytesSize]);
    }
}
