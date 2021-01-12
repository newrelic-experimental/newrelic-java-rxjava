package com.newrelic.instrumentation.rxjava2;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;

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
			Token t = NewRelic.getAgent().getTransaction().getToken();
			if(t != null && t.isActive()) {
				NRRunnable nrRun = new NRRunnable(r, t);
				run = delegate != null ? delegate.apply(nrRun) : nrRun;
			} else {
				t.expire();
				run = delegate != null ? delegate.apply(r) : r;
			}
		}
		return run;
	}

}
