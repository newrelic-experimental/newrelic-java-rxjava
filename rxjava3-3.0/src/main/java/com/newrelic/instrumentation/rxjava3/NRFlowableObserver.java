package com.newrelic.instrumentation.rxjava3;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;


public class NRFlowableObserver<T> implements Subscriber<T>, Subscription {

	private Subscriber<? super T> downstream;
	
	Subscription upstream;
	public NRRxJavaHeaders nrHeaders = null;
	public Segment segment = null;
	
	private static boolean isTransformed = false;
	
	public NRFlowableObserver(Subscriber<? super T> s) {
		downstream = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}
	
	
	@Override
	@Trace(dispatcher=true,excludeFromTransactionTrace=true)
	public void onNext(T t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava3","onNext");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		downstream.onNext(t);
	}

	@Override
	@Trace(dispatcher=true,excludeFromTransactionTrace=true)
	public void onError(Throwable t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava3","onError");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onError(t);
	}

	@Override
	@Trace(dispatcher=true,excludeFromTransactionTrace=true)
	public void onComplete() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava3","onComplete");
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

	@Override
	public void request(long n) {
		upstream.request(n);
	}

	@Override
	public void cancel() {
		upstream.cancel();
		if(segment != null) {
			segment.ignore();
			segment = null;
		}

	}

	@Override
	public void onSubscribe(Subscription s) {
		if(upstream != null) {
			s.cancel();
		} else {
			upstream = s;
			downstream.onSubscribe(this);
		}
	}

}
