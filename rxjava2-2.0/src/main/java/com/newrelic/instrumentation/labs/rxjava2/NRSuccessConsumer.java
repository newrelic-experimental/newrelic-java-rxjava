package com.newrelic.instrumentation.labs.rxjava2;

import io.reactivex.functions.Consumer;

public class NRSuccessConsumer<T> implements Consumer<T> {
	
	private NRRxJava2Holder<?> holder = null;
	
	public NRSuccessConsumer(NRRxJava2Holder<?> h) {
		holder = h;
	}

	@Override
	public void accept(T t) throws Exception {
		if(holder != null) {
			holder.endSegment();
			holder.expireToken();
		}
	}

}
