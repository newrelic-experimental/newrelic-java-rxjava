package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRObservableObserver<T> implements Observer<T>, Disposable {

	private Observer<T> downstream;
	
	private Disposable upstream;
	
	public Token token = null;
	
	public Segment segment = null;
	
	private static boolean isTransformed = false;

	public NRObservableObserver(Observer<T> downstream) {
		this.downstream = downstream;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void dispose() {
		upstream.dispose();
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
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
	public void onError(Throwable e) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onError(e);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onNext(T t) {
		if(token != null) {
			token.link();
		}
		downstream.onNext(t);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onComplete() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

}
