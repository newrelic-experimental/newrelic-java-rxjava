package com.newrelic.instrumentation.labs.rxjava2;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.functions.BiFunction;

@SuppressWarnings("rawtypes")
public class NRSingleSubWrapper implements BiFunction<Single, SingleObserver, SingleObserver> {
	
	private BiFunction<Single, SingleObserver, SingleObserver> delegate = null;
	
	public NRSingleSubWrapper(BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SingleObserver apply(Single t1, SingleObserver t2) throws Exception {
		if(delegate == null) {
			return t2 instanceof NRSingleObserver ? t2 : new NRSingleObserver(t2, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
		}
		SingleObserver actual = delegate.apply(t1, t2);
		return t2 instanceof NRSingleObserver ? actual : new NRSingleObserver(actual, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
	}

	public BiFunction<Single, SingleObserver, SingleObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}
	
	
}
