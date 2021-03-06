package com.newrelic.instrumentation.rxjava3;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.functions.BiFunction;

@SuppressWarnings("rawtypes")
public class NRSingleSubWrapper implements BiFunction<Single, SingleObserver, SingleObserver> {
	
	private BiFunction<Single, SingleObserver, SingleObserver> delegate = null;
	
	public NRSingleSubWrapper(BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SingleObserver apply(Single t1, SingleObserver t2) throws Throwable {
		if(delegate == null) {
			return t2 instanceof NRSingleObserver2 ? t2 : new NRSingleObserver2(t2, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
		}
		SingleObserver actual = delegate.apply(t1, t2);
		return t2 instanceof NRSingleObserver2 ? actual : new NRSingleObserver2(actual, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
	}

	public BiFunction<Single, SingleObserver, SingleObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}
	
	
}
