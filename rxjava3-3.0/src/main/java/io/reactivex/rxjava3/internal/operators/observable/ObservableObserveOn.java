package io.reactivex.rxjava3.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRObservableObserver;

import io.reactivex.rxjava3.core.Observer;

@Weave
public abstract class ObservableObserveOn<T> {

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(Observer<? super T> observer) {
		NRObservableObserver<? super T> wrapper = new NRObservableObserver(observer);
		Token token = NewRelic.getAgent().getTransaction().getToken();
		if(token != null) {
			if(token.isActive()) {
				wrapper.token = token;
			} else {
				token.expire();
				token = null;
			}
		}
		observer = wrapper;
		Weaver.callOriginal();
	}
}
