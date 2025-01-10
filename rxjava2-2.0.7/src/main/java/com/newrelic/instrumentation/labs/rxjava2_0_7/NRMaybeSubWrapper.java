package com.newrelic.instrumentation.labs.rxjava2_0_7;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.functions.BiFunction;

@SuppressWarnings("rawtypes")
public class NRMaybeSubWrapper implements BiFunction<Maybe, MaybeObserver, MaybeObserver> {
	
	private BiFunction<Maybe, MaybeObserver, MaybeObserver> delegate = null;
	
	public NRMaybeSubWrapper(BiFunction<Maybe, MaybeObserver, MaybeObserver> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MaybeObserver apply(Maybe t1, MaybeObserver t2) throws Exception {
		if(delegate == null) {
			return t2 instanceof NRMaybeObserver ? t2 : new NRMaybeObserver(t2, t1.maybeName != null ? t1.maybeName : t1.getClass().getSimpleName());
		}
		MaybeObserver actual = delegate.apply(t1, t2);
		
		return t2 instanceof NRMaybeObserver ? actual : new NRMaybeObserver(actual,t1.maybeName != null ? t1.maybeName : t1.getClass().getSimpleName());
	}

	public BiFunction<Maybe, MaybeObserver, MaybeObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Maybe, MaybeObserver, MaybeObserver> d) {
		delegate = d;
	}
	
}
