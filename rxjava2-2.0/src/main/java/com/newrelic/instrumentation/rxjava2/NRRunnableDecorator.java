package com.newrelic.instrumentation.rxjava2;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.functions.Function;

public class NRRunnableDecorator implements Function<Runnable, Runnable> {

	private Function<Runnable,Runnable> delegate = null;

	public NRRunnableDecorator(Function<Runnable,Runnable> f) {
		delegate = f;
	}

	@Override
	public Runnable apply(Runnable r) throws Exception {
		Runnable run = null;
		if(r instanceof NRRunnable) {
			run = delegate != null ? delegate.apply(r) : r;
		} else {
				 NRRxJavaHeaders hr = new NRRxJavaHeaders();
				 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(hr);

				if(hr != null ) {
					NRRunnable nrRun = new NRRunnable(r, hr);
					run = delegate != null ? delegate.apply(nrRun) : nrRun;
				}
		}
		return run;
	}

}
