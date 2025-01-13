package com.newrelic.instrumentation.labs.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.BiFunction;

@SuppressWarnings("rawtypes")
public class NRObservableSubWrapper implements BiFunction<Observable, Observer, Observer> {
	
	private BiFunction<Observable, Observer, Observer> delegate = null;
	
	public NRObservableSubWrapper(BiFunction<Observable, Observer, Observer> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Observer apply(Observable t1, Observer t2) throws Exception {
		if(delegate == null) {
			return t2 instanceof NRObservableObserver ? t2 : new NRObservableObserver(t2, t1.observableName != null ? t1.observableName : t1.getClass().getSimpleName());
		}
		Observer actual = delegate.apply(t1, t2);
		
		return t2 instanceof NRObservableObserver ? actual : new NRObservableObserver(actual,t1.observableName != null ? t1.observableName : t1.getClass().getSimpleName());
	}

	public BiFunction<Observable, Observer, Observer> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Observable, Observer, Observer> d) {
		delegate = d;
	}
	
}
