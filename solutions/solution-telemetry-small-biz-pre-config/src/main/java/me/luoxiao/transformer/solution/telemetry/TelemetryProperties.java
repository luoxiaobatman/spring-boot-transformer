package me.luoxiao.transformer.solution.telemetry;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("JavadocLinkAsPlainText")
@ConfigurationProperties(prefix = TelemetryProperties.PREFIX)
@Data
public class TelemetryProperties {
    public static final String PREFIX = "me.luoxiao.transformer.solution.telemetry";

    private boolean enabled = false;

    /**
     * for current thread to wait for even if you are using external solutions like Jaeger etc...
     */
    private boolean forceUseSimpleSpanProcessor = false;

    /**
     * Zipkin, Jaeger, Skywalker etc... timeout
     * if timeout, the logging will be discarded
     * <p>
     * omit for logging
     */
    private long timeoutInSecond = 30;

    /**
     * Zipkin, Jaeger, Skywalker etc..., batch size
     */
    private int batchSizePerExport = 1000;

    /**
     * when using solutions like Zipkin, Jaeger, Skywalker etc..., the max cache size for telemetry info queue
     */
    private int queueSize = 10000;

    /**
     * solutions like Zipkin, Jaeger, Skywalker, 2 consecutive export delay.
     */
    private long scheduleDelayInMili = 1000;

    /**
     * eg: http://collect.jaeger.idp/v1/traces
     */
    private String jaegerEndpoint;
}
