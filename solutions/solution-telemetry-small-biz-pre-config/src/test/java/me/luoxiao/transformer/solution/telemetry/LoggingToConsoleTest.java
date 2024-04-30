package me.luoxiao.transformer.solution.telemetry;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"RedundantThrows", "unused"})
@Slf4j
public class LoggingToConsoleTest extends BaseTelemetryTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private Tracer tracer;

    @Test
    @Disabled("assert for logging content")
    public void test() throws Exception {
        Span span = tracer.spanBuilder("spanName").startSpan();
        try (Scope scope = span.makeCurrent()) {
            log.info("logging content");
        } finally {
            span.end();
        }
    }
}
