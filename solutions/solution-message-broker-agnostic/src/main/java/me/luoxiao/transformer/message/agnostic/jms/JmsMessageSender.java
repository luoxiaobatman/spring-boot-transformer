package me.luoxiao.transformer.message.agnostic.jms;

import me.luoxiao.transformer.message.agnostic.BaseMessage;
import me.luoxiao.transformer.message.agnostic.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

public class JmsMessageSender implements MessageSender {
    @Autowired
    private JmsTemplate template;

    @Autowired
    @Qualifier("noPersistJmsTemplate")
    private JmsTemplate noPersistJmsTemplate;

    @Override
    public void sendMessage(String topic, BaseMessage<?> message) {
        template.convertAndSend(topic, message);
    }

    @Override
    public void sendNoPersistMessage(String topic, BaseMessage<?> message) {
        noPersistJmsTemplate.convertAndSend(topic, message);
    }
}
