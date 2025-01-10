package com.newrelic.instrumentation.labs.rxjava1_2;

import rx.Completable.Operator;
import rx.CompletableSubscriber;

public class NRCompletableOperator implements Operator {

	@Override
	public CompletableSubscriber call(CompletableSubscriber t) {
		if(!(t instanceof NRCompletableSubscriber)) {
			NRCompletableSubscriber wrapper = new NRCompletableSubscriber(t);
			return wrapper;
		}
		
		return t;
	}

}
