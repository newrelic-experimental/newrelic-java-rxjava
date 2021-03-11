package com.newrelic.instrumentation.rxjava2;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.flowable.FlowableUtils;

@SuppressWarnings("rawtypes")
public class NRFlowableSubWrapper implements BiFunction<Flowable, Subscriber, Subscriber> {
	
	private BiFunction<Flowable, Subscriber, Subscriber> delegate = null;
	
	public NRFlowableSubWrapper(BiFunction<Flowable, Subscriber, Subscriber> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Subscriber apply(Flowable t1, Subscriber t2) throws Exception {
		boolean ignore = FlowableUtils.ignore(t1);
		if(delegate == null) {
			if(ignore) return t2;
			return t2 instanceof NRFlowableObserver2 ? t2 : new NRFlowableObserver2(t2, t1.flowableName != null ? t1.flowableName : t1.getClass().getSimpleName());
		}
		Subscriber actual = delegate.apply(t1, t2);
		if(ignore) return actual;
		return t2 instanceof NRFlowableObserver2 ? actual : new NRFlowableObserver2(actual, t1.flowableName != null ? t1.flowableName : t1.getClass().getSimpleName());
	}

	public BiFunction<Flowable, Subscriber, Subscriber> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Flowable, Subscriber, Subscriber> d) {
		delegate = d;
	}
}
