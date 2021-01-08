package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRFlowableObserver;

@Weave
public class FlowableDelaySubscriptionOther<T, U> {

	
	public void subscribeActual(Subscriber<? super T> child) {
		NRFlowableObserver<? super T> wrapper = new NRFlowableObserver<T>(child);
		Token token =  NewRelic.getAgent().getTransaction().getToken();
		if(token != null) {
			if(token.isActive()) {
				wrapper.token = token;
			} else {
				token.expire();
				token = null;
			}
		}
		child = wrapper;
		Weaver.callOriginal();
	}
}
