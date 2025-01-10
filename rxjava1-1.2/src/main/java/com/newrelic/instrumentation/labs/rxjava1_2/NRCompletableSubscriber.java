package com.newrelic.instrumentation.labs.rxjava1_2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;

import rx.CompletableSubscriber;
import rx.Subscription;

public class NRCompletableSubscriber implements CompletableSubscriber {

	private CompletableSubscriber actual = null;
	public NRRxJavaHeaders nrHeaders = null;
	private static boolean isTransformed = false;

	public NRCompletableSubscriber(CompletableSubscriber s) {
		actual = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	@Trace(dispatcher=true)
	public void onCompleted() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava1_2","onCompleted");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		actual.onCompleted();
	}

	@Override
	@Trace(dispatcher=true)
	public void onError(Throwable e) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava1_2","onError");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		actual.onError(e);
	}

	@Override
	public void onSubscribe(Subscription d) {

		if(nrHeaders == null) {
			 nrHeaders = new NRRxJavaHeaders();
			 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		}
		actual.onSubscribe(d);
	}

}
