package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Trace;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NRDisposableConsumer implements Consumer<Disposable> {
	
	private NRRxJava2Holder<?> holder = null;
	private static boolean isTransformed = false;
	
	public NRDisposableConsumer(NRRxJava2Holder<?> h) {
		holder = h;
		if(!isTransformed) {
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
			isTransformed = true;
		}
	}

	@Override
	@Trace(async = true)
	public void accept(Disposable t) throws Exception {
		if(holder != null) {
			holder.startSegment();
		}
	}

}
