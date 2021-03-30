package io.reactivex.rxjava3.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRSingleObserver;

import io.reactivex.rxjava3.core.SingleObserver;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Weave
public abstract class SingleTimer {

	
	protected void subscribeActual(SingleObserver<? super Long> s) {
		NRSingleObserver<? super Long> wrapper = new NRSingleObserver(s);
		Token token = NewRelic.getAgent().getTransaction().getToken();
		if(token != null && token.isActive()) {
			wrapper.token = token;
		}
		s = wrapper;
		Weaver.callOriginal();
	}
}
