package com.newrelic.instrumentation.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.observable.ObervableUtils;

@SuppressWarnings("rawtypes")
public class NRObservableSubWrapper implements BiFunction<Observable, Observer, Observer> {
	
	private BiFunction<Observable, Observer, Observer> delegate = null;
	
	public NRObservableSubWrapper(BiFunction<Observable, Observer, Observer> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Observer apply(Observable t1, Observer t2) throws Exception {
		boolean ignore = ObervableUtils.ignore(t1);
		if(delegate == null) {
			if(ignore) return t2;
			return t2 instanceof NRObservableObserver2 ? t2 : new NRObservableObserver2(t2, t1.observableName != null ? t1.observableName : t1.getClass().getSimpleName());
		}
		Observer actual = delegate.apply(t1, t2);
		
		if(ignore) return actual;
		return t2 instanceof NRObservableObserver2 ? actual : new NRObservableObserver2(actual,t1.observableName != null ? t1.observableName : t1.getClass().getSimpleName());
	}

	public BiFunction<Observable, Observer, Observer> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Observable, Observer, Observer> d) {
		delegate = d;
	}
	
}
