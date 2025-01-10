package com.newrelic.instrumentation.labs.rxjava2_0_7;

import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;

public class NRSubscriberConsumer implements Consumer<Subscription> {
	
	private NRRxJava2Holder<?> holder = null;

	public NRSubscriberConsumer(NRRxJava2Holder<?> h) {
		holder = h;
	}

	@Override
	public void accept(Subscription subscription) throws Exception {
		if(holder != null) {
			holder.startSegment();
		}
	}

}
