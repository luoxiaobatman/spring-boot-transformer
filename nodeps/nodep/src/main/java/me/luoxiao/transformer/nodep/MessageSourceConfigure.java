package me.luoxiao.transformer.nodep;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.lang.NonNull;

public interface MessageSourceConfigure extends MessageSourceAware {
    @Override
    default void setMessageSource(@NonNull MessageSource wrapper) {
        MessageSource messageSource;
        if (wrapper instanceof ApplicationContext) {
            try {
                messageSource = ((ApplicationContext) wrapper).getBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
            } catch (Exception ignored) {
                return;
            }
        } else {
            messageSource = wrapper;
        }

        if (messageSource instanceof AbstractResourceBasedMessageSource) {
            String i18nBasename = i18nBasename();
            ((AbstractResourceBasedMessageSource) messageSource).addBasenames(i18nBasename);
            Logger log = logger();
            if (log != null) {
                log.info("config messageSource, add basename: {}", i18nBasename);
            }
        }
    }

    String i18nBasename();

    default Logger logger() {
        return null;
    }
}
