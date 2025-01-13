package com.newrelic.instrumentation.labs.rxjava3;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.rxjava3.functions.Consumer;

public class NRErrorConsumer implements Consumer<Throwable> {
	
	private NRRxJava3Holder<?> holder = null;
	
	public NRErrorConsumer( NRRxJava3Holder<?> h) {
		holder = h;
	}

	@Override
	public void accept(Throwable t) throws Exception {
		NewRelic.noticeError(t);
		if(holder != null) {
			holder.ignoreSegment();
			holder.expireToken();
			holder = null;
		}
	}

}
