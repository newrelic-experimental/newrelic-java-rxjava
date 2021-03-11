package com.newrelic.instrumentation.rxjava2;

import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;

public class NRSingleLift<T> implements SingleOperator<T, T> {

	@Override
	public SingleObserver<? super T> apply(SingleObserver<? super T> observer) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
