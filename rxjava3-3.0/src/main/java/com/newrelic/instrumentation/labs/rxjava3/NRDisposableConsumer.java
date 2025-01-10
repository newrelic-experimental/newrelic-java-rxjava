package com.newrelic.instrumentation.labs.rxjava3;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Trace;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class NRDisposableConsumer implements Consumer<Disposable> {
	
	private NRRxJava3Holder<?> holder = null;
	private static boolean isTransformed = false;
	
	public NRDisposableConsumer(NRRxJava3Holder<?> h) {
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
