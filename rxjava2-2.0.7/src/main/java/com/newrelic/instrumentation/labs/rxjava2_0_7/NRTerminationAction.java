package com.newrelic.instrumentation.labs.rxjava2_0_7;

import io.reactivex.functions.Action;

public class NRTerminationAction implements Action {
	
	private NRRxJava2Holder<?> holder = null;

	public NRTerminationAction(NRRxJava2Holder<?> h) {
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
