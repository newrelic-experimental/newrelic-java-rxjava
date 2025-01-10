package com.newrelic.instrumentation.labs.rxjava2_0_7;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.functions.BiFunction;

public class NRCompletableSubWrapper implements BiFunction<Completable, CompletableObserver, CompletableObserver> {
	
	private BiFunction<Completable, CompletableObserver, CompletableObserver> delegate = null;
	
	public NRCompletableSubWrapper(BiFunction<Completable, CompletableObserver, CompletableObserver> d) {
		delegate = d;
	}

	@Override
	public CompletableObserver apply(Completable t1, CompletableObserver t2) throws Exception {
		if(delegate == null) {
			return t2 instanceof NRCompletableObserver ? t2 : new NRCompletableObserver(t2, t1.completableName != null ? t1.completableName : t1.getClass().getSimpleName());
		}
		CompletableObserver actual = delegate.apply(t1, t2);
		return t2 instanceof NRCompletableObserver ? actual : new NRCompletableObserver(actual, t1.completableName != null ? t1.completableName : t1.getClass().getSimpleName());
	}

	public BiFunction<Completable, CompletableObserver, CompletableObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Completable, CompletableObserver, CompletableObserver> d) {
		delegate = d;
	}
}
