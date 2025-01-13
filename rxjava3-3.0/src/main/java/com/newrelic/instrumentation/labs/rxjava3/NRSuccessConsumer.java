package com.newrelic.instrumentation.labs.rxjava3;

import io.reactivex.rxjava3.functions.Consumer;

public class NRSuccessConsumer<T> implements Consumer<T> {
	
	private NRRxJava3Holder<?> holder = null;
	
	public NRSuccessConsumer(NRRxJava3Holder<?> h) {
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
