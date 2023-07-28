package io.reactivex.rxjava3.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRCompletableObserver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;

import io.reactivex.rxjava3.core.CompletableObserver;

@Weave
public abstract class CompletableObserveOn {

	protected void subscribeActual(CompletableObserver s) {
		NRCompletableObserver wrapper = new NRCompletableObserver(s);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		s = wrapper;
		Weaver.callOriginal();
	}
}
