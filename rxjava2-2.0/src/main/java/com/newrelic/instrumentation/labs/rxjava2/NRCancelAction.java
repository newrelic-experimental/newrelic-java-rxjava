package com.newrelic.instrumentation.labs.rxjava2;

import io.reactivex.functions.Action;

public class NRCancelAction implements Action {
	
	private NRRxJava2Holder<?> holder = null;

	public NRCancelAction(NRRxJava2Holder<?> h) {
		holder = h;
	}

	@Override
	public void run() throws Exception {
		if(holder != null) {
			holder.expireToken();
			holder.ignoreSegment();
		}
	}

}
