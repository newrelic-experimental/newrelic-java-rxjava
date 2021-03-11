package com.newrelic.instrumentation.rxjava2;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.completable.CompletableUtils;

public class NRCompletableSubWrapper implements BiFunction<Completable, CompletableObserver, CompletableObserver> {
	
	private BiFunction<Completable, CompletableObserver, CompletableObserver> delegate = null;
	
	public NRCompletableSubWrapper(BiFunction<Completable, CompletableObserver, CompletableObserver> d) {
		delegate = d;
	}

	@Override
	public CompletableObserver apply(Completable t1, CompletableObserver t2) throws Exception {
		boolean ignore = CompletableUtils.ignore(t1);
		if(delegate == null) {
			if(ignore) return t2;
			return t2 instanceof NRCompletableObserver2 ? t2 : new NRCompletableObserver2(t2, t1.completableName != null ? t1.completableName : t1.getClass().getSimpleName());
		}
		CompletableObserver actual = delegate.apply(t1, t2);
		if(ignore) return actual;
		return t2 instanceof NRCompletableObserver2 ? actual : new NRCompletableObserver2(actual, t1.completableName != null ? t1.completableName : t1.getClass().getSimpleName());
	}

	public BiFunction<Completable, CompletableObserver, CompletableObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Completable, CompletableObserver, CompletableObserver> d) {
		delegate = d;
	}
}
