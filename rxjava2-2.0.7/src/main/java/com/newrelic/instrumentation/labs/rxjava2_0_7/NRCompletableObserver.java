package com.newrelic.instrumentation.labs.rxjava2_0_7;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class NRCompletableObserver implements CompletableObserver {

	private CompletableObserver downstream;

	private String name = null;

	private static boolean isTransformed = false;

	public NRCompletableObserver(CompletableObserver downstream, String n) {
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	@Trace
	public void onSubscribe(Disposable d) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Completable",name,"onSubscribe");
		if(downstream != null) {
			downstream.onSubscribe(d);
		}
	}


	@Override
	@Trace
	public void onError(Throwable e) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Completable",name,"onError");
		NewRelic.noticeError(e);
		if(downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	@Trace
	public void onComplete() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Completable",name,"onError");
		if(downstream != null) {
			downstream.onComplete();
		}
	}


}
