package com.newrelic.instrumentation.labs.rxjava2_0_7;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

@SuppressWarnings("rawtypes")
public class NRFlowableSubWrapper implements BiFunction<Flowable, Subscriber, Subscriber> {
	
	private BiFunction<Flowable, Subscriber, Subscriber> delegate = null;
	
	public NRFlowableSubWrapper(BiFunction<Flowable, Subscriber, Subscriber> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Subscriber apply(Flowable t1, Subscriber t2) throws Exception {
		
		if(delegate == null) {
			return t2 instanceof NRFlowableObserver ? t2 : new NRFlowableObserver(t2, t1.flowableName != null ? t1.flowableName : t1.getClass().getSimpleName(),NewRelic.getAgent().getTransaction());
		}
		Subscriber actual = delegate.apply(t1, t2);
		return t2 instanceof NRFlowableObserver ? actual : new NRFlowableObserver(actual, t1.flowableName != null ? t1.flowableName : t1.getClass().getSimpleName(), NewRelic.getAgent().getTransaction());
	}

	public BiFunction<Flowable, Subscriber, Subscriber> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Flowable, Subscriber, Subscriber> d) {
		delegate = d;
	}
}
