package io.reactivex.rxjava3.internal.operators.maybe;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRMaybeObserver;

import io.reactivex.rxjava3.core.MaybeObserver;

@Weave
public class MaybeSubscribeOn<T> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void subscribeActual(MaybeObserver<? super T> observer) {
		NRMaybeObserver<? super T> wrapper = new NRMaybeObserver(observer);
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
