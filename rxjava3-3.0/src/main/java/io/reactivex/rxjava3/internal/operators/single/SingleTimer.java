package io.reactivex.rxjava3.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;
import com.newrelic.instrumentation.rxjava3.NRSingleObserver;

import io.reactivex.rxjava3.core.SingleObserver;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Weave
public abstract class SingleTimer {


	protected void subscribeActual(SingleObserver<? super Long> s) {
		NRSingleObserver<? super Long> wrapper = new NRSingleObserver(s);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		s = wrapper;
		Weaver.callOriginal();
	}
}
