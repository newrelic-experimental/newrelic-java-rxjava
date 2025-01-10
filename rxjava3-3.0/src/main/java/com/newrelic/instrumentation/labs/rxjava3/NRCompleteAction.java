package com.newrelic.instrumentation.labs.rxjava3;

import io.reactivex.rxjava3.functions.Action;

public class NRCompleteAction implements Action {
	
	private NRRxJava3Holder<?> holder = null;

	public NRCompleteAction(NRRxJava3Holder<?> h) {
		holder = h;
	}

	@Override
	public void run() throws Exception {
		if(holder != null) {
			holder.expireToken();
			holder.endSegment();
		}
	}

}
