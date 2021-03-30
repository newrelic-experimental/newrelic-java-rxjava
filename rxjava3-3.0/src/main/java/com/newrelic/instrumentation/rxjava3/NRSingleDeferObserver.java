package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRSingleDeferObserver<T> implements SingleObserver<T> {

	private SingleObserver<T> downstream;

	private Segment segment = null;

	private static boolean isTransformed = false;

	public NRSingleDeferObserver(SingleObserver<T> downstream) {
		this.downstream = downstream;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Disposable d) {
		downstream.onSubscribe(d);
	}

	public void startSegment(String name) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? name : "SingleDefer");
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
	public void onSuccess(T value) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onSuccess(value);
	}

}
