package io.reactivex.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRCompletableObserver;
import com.newrelic.instrumentation.rxjava2.NRRxJavaHeaders;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;

@Weave
public abstract class CompletableSubscribeOn {
	
	@NewField
	private NRRxJavaHeaders nrHeaders = null;
	
	public CompletableSubscribeOn(CompletableSource source, Scheduler scheduler) {
		if(nrHeaders == null) {
			 nrHeaders = new NRRxJavaHeaders();
			 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
			}
	}

	protected void subscribeActual(CompletableObserver s) {
		NRCompletableObserver wrapper = new NRCompletableObserver(s);
		
		if(nrHeaders == null) {
			 nrHeaders = new NRRxJavaHeaders();
			 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
			}
		s = wrapper;
		Weaver.callOriginal();
	}
}
