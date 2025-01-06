package com.newrelic.instrumentation.rxjava2;

import java.util.logging.Level;

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
		NewRelic.getAgent().getLogger().log(Level.FINE, "Constructed NRFlowableObserver2.<init> with subscriber {0}, name {1}",this.downstream, name);
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
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver2.onError with subscriber {0}, name {1}",this.downstream, name);
		if(segment != null) {
			segment.end();
			segment = null;
		}
		NewRelic.recordResponseTimeMetric("Single-"+name, System.currentTimeMillis()-start);
		downstream.onError(e);
	}

	@Override
	public void onSuccess(T value) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver2.onSuccess with subscriber {0}, name {1}",this.downstream, name);
		if(segment != null) {
			segment.end();
			segment = null;
		}
		NewRelic.recordResponseTimeMetric("Single-"+name, System.currentTimeMillis()-start);
		downstream.onSuccess(value);
	}
	
	public void startSegment() {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver2.startSegment with subscriber {0}, name {1}",this.downstream, name);
		if(!ignore)
			segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Single/"+name : "Single");
	}

}
