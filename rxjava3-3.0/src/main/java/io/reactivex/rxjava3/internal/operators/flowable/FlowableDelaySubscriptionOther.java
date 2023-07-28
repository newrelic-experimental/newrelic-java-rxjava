package io.reactivex.rxjava3.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRFlowableObserver;
import com.newrelic.instrumentation.rxjava3.NRRxJavaHeaders;

@Weave
public class FlowableDelaySubscriptionOther<T, U> {


	public void subscribeActual(Subscriber<? super T> child) {
		NRFlowableObserver<? super T> wrapper = new NRFlowableObserver<T>(child);
		NRRxJavaHeaders  nrHeaders = new NRRxJavaHeaders();
      	NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		child = wrapper;
		Weaver.callOriginal();
	}
}
