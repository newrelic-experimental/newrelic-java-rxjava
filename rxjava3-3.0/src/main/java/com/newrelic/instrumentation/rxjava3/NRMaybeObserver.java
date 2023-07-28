package com.newrelic.instrumentation.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;

import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class NRMaybeObserver<T> implements MaybeObserver<T>, Disposable {

	private MaybeObserver<T> downstream;

	private Disposable upstream;

	public NRRxJavaHeaders nrHeaders = null;
	public Segment segment = null;

	private static boolean isTransformed = false;

	public NRMaybeObserver(MaybeObserver<T> downstream) {
		this.downstream = downstream;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void dispose() {
		upstream.dispose();
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
	}

	@Override
	public boolean isDisposed() {
		return upstream.isDisposed();
	}

	@Override
	public void onSubscribe(Disposable d) {
		if(nrHeaders == null) {
			 nrHeaders = new NRRxJavaHeaders();
			 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		}
		if(upstream != null) {
			d.dispose();
		} else {
			upstream = d;
			downstream.onSubscribe(this);
		}
	}

	@Override
	@Trace(dispatcher=true,excludeFromTransactionTrace=true)
	public void onSuccess(T t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava3","onSuccess");
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
		downstream.onSuccess(t);
	}

	@Override
	@Trace(dispatcher=true,excludeFromTransactionTrace=true)
	public void onError(Throwable e) {
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
		downstream.onError(e);
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

}
