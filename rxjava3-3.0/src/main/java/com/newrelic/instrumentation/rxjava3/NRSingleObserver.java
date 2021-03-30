package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRSingleObserver<T> implements SingleObserver<T>, Disposable {

	private SingleObserver<T> downstream;

	private Disposable upstream;

	private static boolean isTransformed = false;
	
	public Token token = null;
	
	public Segment segment = null;

	public NRSingleObserver(SingleObserver<T> downstream) {
		this.downstream = downstream;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void dispose() {
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
		upstream.dispose();
	}

	@Override
	public boolean isDisposed() {
		return upstream.isDisposed();
	}

	@Override
	public void onSubscribe(Disposable d) {
		if(upstream != null) {
			d.dispose();
		} else {
			upstream = d;
			downstream.onSubscribe(this);
		}
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onSuccess(T t) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
		}
		downstream.onSuccess(t);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onError(Throwable e) {
		NewRelic.noticeError(e);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
		}
		downstream.onError(e);
	}

}
