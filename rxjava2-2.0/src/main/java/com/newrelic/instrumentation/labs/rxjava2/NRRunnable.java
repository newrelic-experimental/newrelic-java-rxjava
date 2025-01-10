package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;


public class NRRunnable implements Runnable {


	public NRRxJavaHeaders nrHeaders = null;
	private Runnable delegate = null;
	private static boolean isTransformed = false;

	public NRRunnable(Runnable r, NRRxJavaHeaders hr) {
		nrHeaders = hr;
		delegate = r;
		if(!isTransformed) {
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
			isTransformed = true;
		}
	}

	@Override
	@Trace(dispatcher=true)
	public void run() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","WrappedRunable", delegate != null ? delegate.getClass().getSimpleName() : "NullRunnable");

		Transaction transaction = NewRelic.getAgent().getTransaction();
		if (transaction != null) {
			if (nrHeaders != null && !nrHeaders.isEmpty()) {
				transaction.acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
			}
		}
		if(delegate != null) {
			delegate.run();
		}
	}

}
