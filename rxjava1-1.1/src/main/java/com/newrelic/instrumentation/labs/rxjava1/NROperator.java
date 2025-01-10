package com.newrelic.instrumentation.labs.rxjava1;

import rx.Observable.Operator;
import rx.Subscriber;

public class NROperator<R> implements Operator<R, R> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Subscriber<? super R> call(Subscriber<? super R> t1) {
		if(!(t1 instanceof NRSubscriber)) {
			NRSubscriber<? super R> wrapper = new NRSubscriber(t1);
			return wrapper;
		}
		return t1;
	}

}
