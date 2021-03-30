package io.reactivex.rxjava3.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRCompletableObserver;

import io.reactivex.rxjava3.core.CompletableObserver;

@Weave
public abstract class CompletableDelay {

	
	protected void subscribeActual(CompletableObserver s) {
		NRCompletableObserver wrapper = new NRCompletableObserver(s);
		Token token =  NewRelic.getAgent().getTransaction().getToken();
		if(token != null) {
			if(token.isActive()) {
				wrapper.token = token;
			} else {
				token.expire();
				token = null;
			}
		}
		s = wrapper;
		Weaver.callOriginal();
	}
}
