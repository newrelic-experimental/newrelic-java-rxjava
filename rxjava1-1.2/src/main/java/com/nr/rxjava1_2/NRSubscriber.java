package com.nr.rxjava1_2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import rx.Subscriber;

public class NRSubscriber<T> extends Subscriber<T> {
	
	Subscriber<T> actual = null;
	Token token = null;
	private static boolean isTransformed = false;
	
	public NRSubscriber(Subscriber<T> a) {
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
		actual = a;
	}

	@Override
	@Trace(async=true)
	public void onCompleted() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		actual.onCompleted();
	}

	@Override
	@Trace(async=true)
	public void onError(Throwable e) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		actual.onError(e);
	}

	@Override
	@Trace(async=true)
	public void onNext(T t) {
		if(token != null) {
			token.link();
		}
		actual.onNext(t);
	}

	@Override
	public void onStart() {
		token = NewRelic.getAgent().getTransaction().getToken();
		actual.onStart();
	}

	
}
