package com.newrelic.instrumentation.rxjava2;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.single.SingleDefer;
import io.reactivex.internal.operators.single.SingleUtils;

@SuppressWarnings("rawtypes")
public class NRSingleSubWrapper implements BiFunction<Single, SingleObserver, SingleObserver> {
	
	private BiFunction<Single, SingleObserver, SingleObserver> delegate = null;
	
	public NRSingleSubWrapper(BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SingleObserver apply(Single t1, SingleObserver t2) throws Exception {
//		boolean ignore = SingleUtils.ignore(t1);
		if(delegate == null) {
//			if(ignore) return t2;
			return t2 instanceof NRSingleObserver2 ? t2 : new NRSingleObserver2(t2, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
		}
		SingleObserver actual = delegate.apply(t1, t2);
//		if(ignore) return actual;
		return t2 instanceof NRSingleObserver2 ? actual : new NRSingleObserver2(actual, t1.singleName != null ? t1.singleName : t1.getClass().getSimpleName());
	}

	public BiFunction<Single, SingleObserver, SingleObserver> getDelegate() {
		return delegate;
	}
	
	public void setDelegate( BiFunction<Single, SingleObserver, SingleObserver> d) {
		delegate = d;
	}
	
	
}
