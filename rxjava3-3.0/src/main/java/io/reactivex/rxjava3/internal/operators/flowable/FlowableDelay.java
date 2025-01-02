package io.reactivex.rxjava3.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRFlowableObserver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;

@Weave
public abstract class FlowableDelay<T> {

	protected void subscribeActual(Subscriber<? super T> t) {
		NRFlowableObserver<? super T> wrapper = new NRFlowableObserver<T>(t);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		t = wrapper;
		Weaver.callOriginal();
	}
}
