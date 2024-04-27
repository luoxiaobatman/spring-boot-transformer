package me.luoxiao.transformer.support.testing.annotation.meta;

import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySource(properties = {
        "logging.level.me.luoxiao=debug",
})
public @interface TestPropertiesLoggingDebug {
}
