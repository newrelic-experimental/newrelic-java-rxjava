package com.newrelic.instrumentation.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NRSingleObserver2<T> implements SingleObserver<T> {

	private SingleObserver<T> downstream;

	private Segment segment = null;
	private String name = null;
	public boolean ignore = false;
	private long start;

	private static boolean isTransformed = false;

	public NRSingleObserver2(SingleObserver<T> downstream, String n) {
		this.downstream = downstream;
		name = n;
		start = System.currentTimeMillis();
//		if(!ignore)
//			startSegment();
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Disposable d) {
		downstream.onSubscribe(d);
	}


	@Override
	public void onError(Throwable e) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		NewRelic.recordResponseTimeMetric("Single-"+name, System.currentTimeMillis()-start);
		downstream.onError(e);
	}

	@Override
	public void onSuccess(T value) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		NewRelic.recordResponseTimeMetric("Single-"+name, System.currentTimeMillis()-start);
		downstream.onSuccess(value);
	}
	
	public void startSegment() {
		if(!ignore)
			segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Single/"+name : "Single");
	}

}
