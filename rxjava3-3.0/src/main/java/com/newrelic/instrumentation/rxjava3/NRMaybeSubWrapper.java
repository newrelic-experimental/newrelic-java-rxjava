package com.newrelic.instrumentation.rxjava3;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.internal.operators.maybe.MaybeUtils;

@SuppressWarnings("rawtypes")
public class NRMaybeSubWrapper implements BiFunction<Maybe, MaybeObserver, MaybeObserver> {
	
	private BiFunction<Maybe, MaybeObserver, MaybeObserver> delegate = null;
	
	public NRMaybeSubWrapper(BiFunction<Maybe, MaybeObserver, MaybeObserver> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MaybeObserver apply(Maybe t1, MaybeObserver t2) throws Throwable {
		boolean ignore = MaybeUtils.ignore(t1);
		if(delegate == null) {
			if(ignore) return t2;
			return t2 instanceof NRMaybeObserver2 ? t2 : new NRMaybeObserver2(t2, t1.maybeName != null ? t1.maybeName : t1.getClass().getSimpleName());
		}
		MaybeObserver actual = delegate.apply(t1, t2);
		
		if(ignore) return actual;
		return t2 instanceof NRMaybeObserver2 ? actual : new NRMaybeObserver2(actual,t1.maybeName != null ? t1.maybeName : t1.getClass().getSimpleName());
	}

	public BiFunction<Maybe, MaybeObserver, MaybeObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Maybe, MaybeObserver, MaybeObserver> d) {
		delegate = d;
	}
	
}
