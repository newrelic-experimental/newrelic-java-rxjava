package io.reactivex.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRObservableObserver;
import com.newrelic.instrumentation.rxjava2.NRRxJavaHeaders;

import io.reactivex.Observer;

@Weave
public abstract class ObservableObserveOn<T> {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(Observer<? super T> observer) {
		NRObservableObserver<? super T> wrapper = new NRObservableObserver(observer);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		observer = wrapper;
		Weaver.callOriginal();
	}
}
