package com.newrelic.instrumentation.labs.rxjava2_0_7;

import io.reactivex.functions.Consumer;

public class NRNextConsumer<T> implements Consumer<T> {
	
	private NRRxJava2Holder<?> holder = null;
	
	public NRNextConsumer(NRRxJava2Holder<?> h) {
		holder = h;
	}

	@Override
	public void accept(T t) throws Exception {
		if(holder != null) {
			holder.linkToken();
		}
	}

}
