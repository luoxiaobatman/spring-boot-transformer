package me.luoxiao.transformer.solution.telemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.ServiceAttributes;
import io.opentelemetry.semconv.TelemetryAttributes;
import lombok.extern.slf4j.Slf4j;
import me.luoxiao.transformer.nodep.MessageSourceConfigure;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AutoConfiguration
@EnableConfigurationProperties({
        TelemetryProperties.class
})
@ConditionalOnProperty(prefix = TelemetryProperties.PREFIX, name = "enabled", havingValue = "true")
@Slf4j
public class TelemetryAutoConfiguration implements MessageSourceConfigure {
    public static final String BEAN_NAME_OPEN_TELEMETRY = "openTelemetry";
    public static final String BEAN_NAME_LOGGING_SPAN_EXPORTER = "loggingSpanExporter";
    public static final String BEAN_NAME_JAEGER_SPAN_EXPORTER = "jaegerSpanExporter";
    public static final String I18N_BASENAME = "i18n/me/luoxiao/transformer/solution/telemetry/messages";

    @Autowired
    private TelemetryProperties properties;

    @Bean
    Tracer idpWebTracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("root");
    }

    @Bean(BEAN_NAME_OPEN_TELEMETRY)
    @ConditionalOnMissingBean(name = BEAN_NAME_OPEN_TELEMETRY)
    public OpenTelemetry openTelemetry(
            @Autowired(required = false)
            List<SpanExporter> spanExporterList) {
        spanExporterList = Objects.requireNonNullElse(spanExporterList, new ArrayList<>());
        SpanProcessor spanProcessor = SpanProcessor.composite(
                spanExporterList.stream().map(spanExporter -> {
                            if (spanExporter instanceof LoggingSpanExporter || properties.isForceUseSimpleSpanProcessor()) {
                                return SimpleSpanProcessor.create(spanExporter);
                            } else {
                                return BatchSpanProcessor.builder(spanExporter)
                                        .setExporterTimeout(properties.getTimeoutInSecond(), TimeUnit.SECONDS)
                                        .setScheduleDelay(properties.getScheduleDelayInMili(), TimeUnit.MILLISECONDS)
                                        .setMaxExportBatchSize(properties.getBatchSizePerExport())
                                        .setMaxQueueSize(properties.getQueueSize())
                                        .build();
                            }
                        }
                ).collect(Collectors.toList())
        );

        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.of(
                        ServiceAttributes.SERVICE_NAME, "YOUR_PLATFORM",
                        TelemetryAttributes.TELEMETRY_SDK_LANGUAGE, "java")));

        return OpenTelemetrySdk.builder()
                .setTracerProvider(
                        SdkTracerProvider.builder()
                                .addSpanProcessor(spanProcessor)
                                .setResource(resource)
                                .build()
                )
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();
    }

    @Bean(name = BEAN_NAME_LOGGING_SPAN_EXPORTER)
    @ConditionalOnProperty(prefix = TelemetryProperties.PREFIX, name = "exporter", havingValue = "logging")
    SpanExporter loggingSpanExporter() {
        return LoggingSpanExporter.create();
    }

    @Bean(name = BEAN_NAME_JAEGER_SPAN_EXPORTER)
    @ConditionalOnProperty(prefix = TelemetryProperties.PREFIX, name = "exporter", havingValue = "jaeger")
    SpanExporter jeagerSpanExporter() {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint(properties.getJaegerEndpoint())
                .build();
    }

    @Override
    public String i18nBasename() {
        return I18N_BASENAME;
    }

    @Override
    public Logger logger() {
        return log;
    }
}
