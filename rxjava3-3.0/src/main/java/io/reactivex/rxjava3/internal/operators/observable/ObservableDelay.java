package io.reactivex.rxjava3.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRObservableObserver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;

import io.reactivex.rxjava3.core.Observer;

@Weave
public abstract class ObservableDelay<T> {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribeActual(Observer<? super T> t) {
		NRObservableObserver<? super T> wrapper = new NRObservableObserver(t);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		t = wrapper;
		Weaver.callOriginal();
	}
}
