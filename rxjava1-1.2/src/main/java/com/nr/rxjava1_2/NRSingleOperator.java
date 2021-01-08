package com.nr.rxjava1_2;

import rx.Observable.Operator;
import rx.Subscriber;

public class NRSingleOperator<T> implements Operator<T,T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Subscriber<? super T> call(Subscriber<? super T> t) {
		if(!(t instanceof NRSubscriber)) {
			NRSubscriber<? super T> wrapper = new NRSubscriber(t);
			return wrapper;
		}
		return t;
	}

}
