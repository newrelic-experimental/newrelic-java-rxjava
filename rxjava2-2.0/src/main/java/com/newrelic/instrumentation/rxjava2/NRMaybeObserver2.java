package com.newrelic.instrumentation.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class NRMaybeObserver2<T> implements MaybeObserver<T> {

	private MaybeObserver<T> downstream;
	
	private Segment segment = null;
	
	private String name = null;
	
	private static boolean isTransformed = false;

	public NRMaybeObserver2(MaybeObserver<T> downstream, String n) {
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Disposable d) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Maybe/"+ name : "Maybe");
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
	public void onComplete() {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

	@Override
	public void onSuccess(T value) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();		
	}

}
