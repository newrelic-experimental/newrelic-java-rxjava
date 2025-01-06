package com.newrelic.instrumentation.rxjava2;

import java.util.logging.Level;

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
		NewRelic.getAgent().getLogger().log(Level.FINE, "Constructed NRFlowableObserver2.<init> with subscriber {0} and name {1}",this.downstream, name);
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		if(downstream != null) {
			segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Flowable/"+name : "Flowable");
		}
		if(downstream != null) {
			downstream.onSubscribe(s);
		}
	}


	@Override
	public void onError(Throwable e) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver2.onError with subscriber {0}, name {1}",downstream,name);
		if(segment != null) {
			segment.end();
			segment = null;
		}
		if (downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	public void onComplete() {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserve2.onComplete with subscriber {0}, name {1}",downstream,name);
		if(segment != null) {
			segment.end();
			segment = null;
		}
		if (downstream != null) {
			downstream.onComplete();
		}
	}

	@Override
	public void onNext(T t) {
		if (downstream != null) {
			downstream.onNext(t);
		}
	}

}
