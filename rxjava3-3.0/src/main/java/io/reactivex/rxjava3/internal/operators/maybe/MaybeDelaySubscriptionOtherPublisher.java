package io.reactivex.rxjava3.internal.operators.maybe;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRMaybeObserver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;

import io.reactivex.rxjava3.core.MaybeObserver;

@Weave
public class MaybeDelaySubscriptionOtherPublisher<T, U> {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void subscribeActual(MaybeObserver<? super T> observer) {
		NRMaybeObserver<? super T> wrapper = new NRMaybeObserver(observer);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		observer = wrapper;
		Weaver.callOriginal();
	}
}
