package cm.packagemanager.pmanager.administrator.tracing;

import com.uber.jaeger.Configuration;


import com.uber.jaeger.samplers.ProbabilisticSampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class OpenTracingConfig {

	@Value("${jaeger.agent.host}")
	private String agentHost;

	@Value("${jaeger.agent.port}")
	private int agentport;


	/*@Bean
	public io.opentracing.Tracer jaegerTracer(){
		return new Configuration(
				"my Service",
				new Configuration.SamplerConfiguration("const", 1),
				new Configuration.ReporterConfiguration(
						false, agentHost, null, 1000, 10000)
		).getTracer();
	}*/

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		return new Configuration("PManagerService", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
				new Configuration.ReporterConfiguration())
				.getTracer();
	}

}
