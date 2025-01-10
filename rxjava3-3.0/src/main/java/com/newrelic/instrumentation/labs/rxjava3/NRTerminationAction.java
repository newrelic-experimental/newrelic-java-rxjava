package com.newrelic.instrumentation.labs.rxjava3;

import io.reactivex.rxjava3.functions.Action;

public class NRTerminationAction implements Action {
	
	private NRRxJava3Holder<?> holder = null;

	public NRTerminationAction(NRRxJava3Holder<?> h) {
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
