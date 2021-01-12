package com.newrelic.instrumentation.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

public class NRRunnable implements Runnable {
	
	
	private Token token = null;
	private Runnable delegate = null;
	private static boolean isTransformed = false;

	public NRRunnable(Runnable r, Token t) {
		token = t;
		delegate = r;
		if(!isTransformed) {
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());;
			isTransformed = true;
		}
	}

	@Override
	@Trace(async=true)
	public void run() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(delegate != null) {
			delegate.run();
		}
	}

}
