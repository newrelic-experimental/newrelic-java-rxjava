package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NRObservableObserver<T> implements Observer<T> {

	private Observer<T> downstream;
	
	private String name = null;
	
	private static boolean isTransformed = false;

	public NRObservableObserver(Observer<T> downstream, String n) {
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
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Observable",name,"onSubscribe");
		if(downstream != null) {
			downstream.onSubscribe(d);
		}
	}


	@Override
	@Trace
	public void onError(Throwable e) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Observable",name,"onError");
		NewRelic.noticeError(e);
		if(downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	@Trace
	public void onNext(T t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Observable",name,"onNext");
		if(downstream != null) {
			downstream.onNext(t);
		}
	}

	@Override
	@Trace
	public void onComplete() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Observable",name,"onComplete");
		if(downstream != null) {
			downstream.onComplete();
		}
	}

}
