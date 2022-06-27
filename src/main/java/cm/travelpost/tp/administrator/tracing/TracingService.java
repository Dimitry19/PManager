package cm.travelpost.tp.administrator.tracing.service;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TracingService {

	@Qualifier("jaegerTracer")
	@Autowired
	protected Tracer gTracer;

	protected Span travelpostSpan;


	@PostConstruct
	public void init() {
		if (!GlobalTracer.isRegistered()){
			GlobalTracer.register(gTracer);
		}

	}

	public void createOpentracingSpan(String spanName) {
		travelpostSpan = GlobalTracer.get().buildSpan(spanName).start();
	}

	public void finishOpentracingSpan() {
		if (travelpostSpan != null) {
			travelpostSpan.finish();
		}
	}

	public void setSpanTag(String tagName, String value) {
		travelpostSpan.setTag(tagName,value);
	}
}
