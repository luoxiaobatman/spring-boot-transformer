package me.luoxiao.transformer.solution.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;

/**
 * feign support for telemetry
 */
public class IdpTelemetryTraceparentRequestInterceptor implements RequestInterceptor {
    private static final TextMapSetter<RequestTemplate> requestTemplateTraceSetter = (carrier, key, value) -> {
        assert carrier != null;
        carrier.header(key, value);
    };

    @Override
    public void apply(RequestTemplate template) {
        W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), template, requestTemplateTraceSetter);
    }
}
