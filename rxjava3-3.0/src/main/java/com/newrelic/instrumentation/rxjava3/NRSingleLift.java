package com.newrelic.instrumentation.rxjava3;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOperator;

public class NRSingleLift<T> implements SingleOperator<T, T> {

	@Override
	public SingleObserver<? super T> apply(SingleObserver<? super T> observer) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
