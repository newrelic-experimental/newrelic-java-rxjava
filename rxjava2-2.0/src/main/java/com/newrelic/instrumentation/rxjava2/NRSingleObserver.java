package com.newrelic.instrumentation.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NRSingleObserver<T> implements SingleObserver<T>, Disposable {

	private SingleObserver<T> downstream;

	private Disposable upstream;

	private static boolean isTransformed = false;
	
	public Token token = null;

	public NRSingleObserver(SingleObserver<T> downstream) {
		this.downstream = downstream;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void dispose() {
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
	@Trace(async=true/*,excludeFromTransactionTrace=true*/)
	public void onSuccess(T t) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		downstream.onSuccess(t);
	}

	@Override
	@Trace(async=true/*,excludeFromTransactionTrace=true*/)
	public void onError(Throwable e) {
		NewRelic.noticeError(e);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		downstream.onError(e);
	}

}
