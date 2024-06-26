package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRFlowableObserver;
import com.newrelic.instrumentation.rxjava2.NRRxJavaHeaders;

@Weave
public abstract class FlowableSubscribeOn<T> {

	public void subscribeActual(Subscriber<? super T> t) {
		NRFlowableObserver<? super T> wrapper = new NRFlowableObserver<T>(t);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		t = wrapper;
		Weaver.callOriginal();
	}

}
