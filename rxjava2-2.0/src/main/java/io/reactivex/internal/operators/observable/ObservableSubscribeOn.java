package io.reactivex.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRObservableObserver;

import io.reactivex.Observer;

@Weave
public class ObservableSubscribeOn<T> {

	
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
