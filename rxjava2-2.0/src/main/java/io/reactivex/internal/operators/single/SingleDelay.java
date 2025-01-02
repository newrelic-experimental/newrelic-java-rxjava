package io.reactivex.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRRxJavaHeaders;
import com.newrelic.instrumentation.rxjava2.NRSingleObserver;

import io.reactivex.SingleObserver;

@Weave
public class SingleDelay<T> {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(SingleObserver<? super T> s) {
		NRSingleObserver<? super T> wrapper = new NRSingleObserver(s);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		s = wrapper;
		Weaver.callOriginal();
	}
}
