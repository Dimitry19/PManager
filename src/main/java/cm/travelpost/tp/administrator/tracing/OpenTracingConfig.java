package cm.travelpost.tp.administrator.tracing;


import com.uber.jaeger.samplers.ProbabilisticSampler;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenTracingConfig {

    @Value("${jaeger.agent.host}")
    private String agentHost;

    @Value("${jaeger.agent.port}")
    private int agentport;


    @Bean
    public Span spanTracer() {

        if(!GlobalTracer.isRegistered()){
            GlobalTracer.register(jaegerTracer());
        }

        return GlobalTracer.get().buildSpan("").start();
    }

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        return new com.uber.jaeger.Configuration("TravelPostService", new com.uber.jaeger.Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
                new com.uber.jaeger.Configuration
                        .ReporterConfiguration())
                .getTracer();
    }

}
