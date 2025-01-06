package io.reactivex.internal.operators.flowable;

import java.util.logging.Level;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.instrumentation.rxjava2.NRFlowableObserver;

import io.reactivex.Flowable;

public class FlowableUtils {

	public static <T> boolean ignore(Flowable<T> flowable) {
		
		if(flowable instanceof AbstractFlowableWithUpstream) {
			return true;
		}
		
		return false;
	}
	
	public static <T> NRFlowableObserver<T> getWrapper(Subscriber<T> actual, String name) {
		if(!(actual instanceof NRFlowableObserver)) {
			NRFlowableObserver<T> wrapper = new NRFlowableObserver<>(actual);
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.setSegment(segment);
			NewRelic.getAgent().getLogger().log(Level.FINE, "Created NRFlowableObserver with subscriber {0} and segment {1} with name {2}", actual, segment, name);
			return wrapper;
		}
		return null;
	}
}
