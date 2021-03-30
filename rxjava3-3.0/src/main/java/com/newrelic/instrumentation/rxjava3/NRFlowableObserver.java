package com.newrelic.instrumentation.rxjava3;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;


public class NRFlowableObserver<T> implements Subscriber<T>, Subscription {

	private Subscriber<? super T> downstream;
	
	Subscription upstream;
	public Token token = null;
	public Segment segment = null;
	
	private static boolean isTransformed = false;
	
	public NRFlowableObserver(Subscriber<? super T> s) {
		downstream = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
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
	public void onError(Throwable t) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onError(t);
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

	@Override
	public void request(long n) {
		upstream.request(n);
	}

	@Override
	public void cancel() {
		upstream.cancel();
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
		if(token != null) {
			token.expire();
			token = null;
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		if(upstream != null) {
			s.cancel();
		} else {
			upstream = s;
			downstream.onSubscribe(this);
		}
	}

}
