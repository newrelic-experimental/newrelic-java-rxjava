package com.newrelic.instrumentation.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class NRMaybeObserver<T> implements MaybeObserver<T>, Disposable {
	
	private MaybeObserver<T> downstream;
	
	private Disposable upstream;
	
	public Token token = null;
	private static boolean isTransformed = false;

	public NRMaybeObserver(MaybeObserver<T> downstream) {
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
		if(token == null) {
			token = NewRelic.getAgent().getTransaction().getToken();
		}
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
		downstream.onSuccess(t);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onError(Throwable e) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		downstream.onError(e);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onComplete() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		downstream.onComplete();
	}

}
