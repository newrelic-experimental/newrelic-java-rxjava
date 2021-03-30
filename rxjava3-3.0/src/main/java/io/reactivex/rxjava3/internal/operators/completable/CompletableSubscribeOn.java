package io.reactivex.rxjava3.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRCompletableObserver;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Scheduler;

@Weave
public abstract class CompletableSubscribeOn {
	
	@NewField
	private Token token = null;
	
	public CompletableSubscribeOn(CompletableSource source, Scheduler scheduler) {
		if(token == null) {
			Token t = NewRelic.getAgent().getTransaction().getToken();
			if(t != null && t.isActive()) {
				token = t;
			} else if(t != null) {
				t.expire();
				t = null;
			}
		}
	}

	protected void subscribeActual(CompletableObserver s) {
		NRCompletableObserver wrapper = new NRCompletableObserver(s);
		
		Token t =  token != null ? token : NewRelic.getAgent().getTransaction().getToken();
		if(t != null) {
			if(t.isActive()) {
				wrapper.token = t;
			} else {
				t.expire();
				t = null;
			}
		}
		s = wrapper;
		token = null;
		Weaver.callOriginal();
	}
}
