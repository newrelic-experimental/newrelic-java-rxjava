package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class NRMaybeObserver<T> implements MaybeObserver<T> {

	private MaybeObserver<T> downstream;
	
	private String name = null;
	
	private static boolean isTransformed = false;

	public NRMaybeObserver(MaybeObserver<T> downstream, String n) {
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
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Maybe",name,"onSubscribe");
		if(downstream != null) {
			downstream.onSubscribe(d);
		}
	}


	@Override
	@Trace
	public void onError(Throwable e) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Maybe",name,"onError");
		NewRelic.noticeError(e);
		if(downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	@Trace
	public void onComplete() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Maybe",name,"onComplete");
		if(downstream != null) {
			downstream.onComplete();
		}
	}

	@Override
	@Trace
	public void onSuccess(T value) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Maybe",name,"onSuccess");
		if(downstream != null) {
			downstream.onSuccess(value);		
		}
	}

}
