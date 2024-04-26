package me.luoxiao.transformer.solution.feign;

import me.luoxiao.transformer.nodep.MessageSourceConfigure;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        FeignProperties.class
})
public class FeignAutoConfiguration implements MessageSourceConfigure {

    public static final String I18N_BASENAME = "i18n/me/luoxiao/transformer/solution/feign";

    @Override
    public String i18nBasename() {
        return I18N_BASENAME;
    }
}
