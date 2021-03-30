package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRObservableObserver2<T> implements Observer<T> {

	private Observer<T> downstream;
	
	private Segment segment = null;
	
	private String name = null;
	
	private static boolean isTransformed = false;

	public NRObservableObserver2(Observer<T> downstream, String n) {
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Disposable d) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Observable/"+ name : "Observable");
		downstream.onSubscribe(d);
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
	public void onNext(T t) {
		downstream.onNext(t);
	}

	@Override
	public void onComplete() {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

}
