package io.reactivex.rxjava3.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRSingleObserver;

import io.reactivex.rxjava3.core.SingleObserver;

@Weave
public abstract class SingleSubscribeOn<T> {

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(SingleObserver<? super T> s) {
		NRSingleObserver<? super T> wrapper = new NRSingleObserver(s);
		Token token = NewRelic.getAgent().getTransaction().getToken();
		if(token != null && token.isActive()) {
			wrapper.token = token;
		}
		s = wrapper;
		Weaver.callOriginal();
	}
}
