package com.newrelic.instrumentation.labs.test.rxjava2;

import java.util.concurrent.Callable;

import com.newrelic.api.agent.Trace;

public class MyCallable implements Callable<String> {
	
	private String retValue = "Blue";

	@Override
	@Trace
	public String call() throws Exception {
		return retValue;
	}

}
