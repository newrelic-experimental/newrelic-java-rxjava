package com.newrelic.instrumentation.labs.rxjava2_0_7;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.functions.Consumer;

public class NRErrorConsumer implements Consumer<Throwable> {
	
	private NRRxJava2Holder<?> holder = null;
	
	public NRErrorConsumer( NRRxJava2Holder<?> h) {
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
