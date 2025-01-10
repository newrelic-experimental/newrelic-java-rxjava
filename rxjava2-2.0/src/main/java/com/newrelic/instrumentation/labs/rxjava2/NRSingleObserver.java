package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NRSingleObserver<T> implements SingleObserver<T> {

	private SingleObserver<T> downstream;

	private String name = null;

	private static boolean isTransformed = false;

	public NRSingleObserver(SingleObserver<T> downstream, String n) {
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
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Single",name,"onSubscribe");
		if(downstream != null) {
			downstream.onSubscribe(d);
		}
	}


	@Override
	@Trace
	public void onError(Throwable e) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Single",name,"onError");
		NewRelic.noticeError(e);
		if(downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	@Trace
	public void onSuccess(T value) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Single",name,"onSuccess");
		if(downstream != null) {
			downstream.onSuccess(value);
		}
	}
	

}
