package com.newrelic.instrumentation.labs.rxjava1_2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;

import rx.Subscriber;

public class NRSubscriber<T> extends Subscriber<T> {
	
	Subscriber<T> actual = null;
	public NRRxJavaHeaders nrHeaders = null;
	private static boolean isTransformed = false;
	
	public NRSubscriber(Subscriber<T> a) {
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
		actual = a;
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
	@Trace(dispatcher=true)
	public void onNext(T t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava1_2","onNext");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		actual.onNext(t);
	}

	@Override
	public void onStart() {
		if(nrHeaders == null) {
			 nrHeaders = new NRRxJavaHeaders();
			 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		}
		actual.onStart();
	}

	
}
