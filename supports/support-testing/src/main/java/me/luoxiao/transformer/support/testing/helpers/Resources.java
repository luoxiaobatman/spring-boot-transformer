package me.luoxiao.transformer.support.testing.helpers;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Resources {
    private static final ResourceLoader rl = new DefaultResourceLoader();
    public static Resource of(String path) {
        return rl.getResource(path);
    }
    public static Resource ofClassPath(String path) {
        return of(ResourceLoader.CLASSPATH_URL_PREFIX + path);
    }
    public static Resource ofFile(String path) {
        return of("file:" + path);
    }
}
