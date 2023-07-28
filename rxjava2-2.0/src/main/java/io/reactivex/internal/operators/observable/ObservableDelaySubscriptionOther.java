package io.reactivex.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRObservableObserver;
import com.newrelic.instrumentation.rxjava2.NRRxJavaHeaders;

import io.reactivex.Observer;

@Weave
public abstract class ObservableDelaySubscriptionOther<T, U> {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribeActual(Observer<? super T> child) {
		NRObservableObserver<? super T> wrapper = new NRObservableObserver(child);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		child = wrapper;
		Weaver.callOriginal();
	}
}
