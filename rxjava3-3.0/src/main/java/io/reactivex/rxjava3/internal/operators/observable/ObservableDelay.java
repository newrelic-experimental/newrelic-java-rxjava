package io.reactivex.rxjava3.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRObservableObserver;

import io.reactivex.rxjava3.core.Observer;

@Weave
public abstract class ObservableDelay<T> {

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribeActual(Observer<? super T> t) {
		NRObservableObserver<? super T> wrapper = new NRObservableObserver(t);
		Token token = NewRelic.getAgent().getTransaction().getToken();
		if(token != null) {
			if(token.isActive()) {
				wrapper.token = token;
			} else {
				token.expire();
				token = null;
			}
		}
		t = wrapper;
		Weaver.callOriginal();
	}
}
