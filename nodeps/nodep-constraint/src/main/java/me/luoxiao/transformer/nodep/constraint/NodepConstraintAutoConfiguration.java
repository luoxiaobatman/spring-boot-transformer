package me.luoxiao.transformer.nodep.constraint;

import lombok.extern.slf4j.Slf4j;
import me.luoxiao.transformer.nodep.MessageSourceConfigure;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@Slf4j
@AutoConfiguration
public class NodepConstraintAutoConfiguration implements MessageSourceConfigure {
    public static final String I18N_BASENAME = "i18n/me/luoxiao/transformer/nodep/constraint";

    @Override
    public String i18nBasename() {
        return I18N_BASENAME;
    }

    @Override
    public Logger logger() {
        return log;
    }
}
