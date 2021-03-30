package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRCompletableObserver2 implements CompletableObserver {

	private CompletableObserver downstream;

	private Segment segment = null;
	private String name = null;

	private static boolean isTransformed = false;

	public NRCompletableObserver2(CompletableObserver downstream, String n) {
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Disposable d) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name != null ? "Completable/"+name : "Completable");
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


}
