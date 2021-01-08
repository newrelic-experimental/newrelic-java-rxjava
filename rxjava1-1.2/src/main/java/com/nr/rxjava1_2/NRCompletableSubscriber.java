package com.nr.rxjava1_2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import rx.CompletableSubscriber;
import rx.Subscription;

public class NRCompletableSubscriber implements CompletableSubscriber {
	
	private CompletableSubscriber actual = null;
	Token token = null;
	private static boolean isTransformed = false;
	
	public NRCompletableSubscriber(CompletableSubscriber s) {
		actual = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
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
	public void onSubscribe(Subscription d) {
		Token t = NewRelic.getAgent().getTransaction().getToken();
		if(t != null) {
			if(t.isActive()) {
				token = t;
			} else {
				t.expire();
				t = null;
			}
		}
		actual.onSubscribe(d);
	}

}
