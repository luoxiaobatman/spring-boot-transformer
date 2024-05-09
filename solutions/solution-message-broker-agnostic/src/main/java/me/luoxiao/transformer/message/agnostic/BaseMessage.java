package me.luoxiao.transformer.message.agnostic;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseMessage<T> implements Serializable {

    private static final long serialVersionUID = -9096785760756990286L;

//    private String id = RandomStringUtils.randomAlphanumeric(16);
    private String id = "123456789";

    private Date time = new Date();

    private String msg;

    private T data;

    private MessageResult result = MessageResult.SUCCESS;

    private Map<String, Object> extraInfo = new HashMap<>();

    private String dataClass;


    public BaseMessage() {

    }

    public BaseMessage(String msg) {
        this.msg = msg;
    }

    public BaseMessage(T data) {
        this.data = data;
    }

    public BaseMessage(String msg, T data) {
        this.data = data;
        this.msg = msg;
    }

    public BaseMessage(T data, MessageResult result) {
        this.data = data;
        this.result = result;
    }

    public BaseMessage(T data, Map<String, Object> extraInfo) {
        this.data = data;
        this.extraInfo = extraInfo;
    }

    public BaseMessage(T data, MessageResult result, Map<String, Object> extraInfo) {
        this.data = data;
        this.result = result;
        this.extraInfo = extraInfo;
    }

    public BaseMessage(String id, Date time, T data, MessageResult result, Map<String, Object> extraInfo) {
        this.id = id;
        this.time = time;
        this.data = data;
        this.result = result;
        this.extraInfo = extraInfo;
    }
}
