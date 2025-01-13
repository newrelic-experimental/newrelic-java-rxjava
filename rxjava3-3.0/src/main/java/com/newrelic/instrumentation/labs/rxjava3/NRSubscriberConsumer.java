package com.newrelic.instrumentation.labs.rxjava3;

import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.functions.Consumer;

public class NRSubscriberConsumer implements Consumer<Subscription> {
	
	private NRRxJava3Holder<?> holder = null;

	public NRSubscriberConsumer(NRRxJava3Holder<?> h) {
		holder = h;
	}

	@Override
	public void accept(Subscription subscription) throws Exception {
		if(holder != null) {
			holder.startSegment();
		}
	}

}
