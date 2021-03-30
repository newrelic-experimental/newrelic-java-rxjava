package com.newrelic.instrumentation.rxjava2;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

public class NRFlowableObserver2<T> implements Subscriber<T> {

	private Subscriber<? super T> downstream;

	private Segment segment = null;
	private String name = null;

	private static boolean isTransformed = false;

	public NRFlowableObserver2(Subscriber<? super T> downstream, String n) {
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Flowable/"+name : "Flowable");
		downstream.onSubscribe(s);
	}


	@Override
	public void onError(Throwable e) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onError(e);
	}

	@Override
	public void onComplete() {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

	@Override
	public void onNext(T t) {
		downstream.onNext(t);
	}

}
