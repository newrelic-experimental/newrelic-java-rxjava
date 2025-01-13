package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.Completable;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.completable.CompletablePeek;

public class NRCompletableAssembly implements Function<Completable, Completable> {
	
	private static final String FROM_PREFIX = "CompletableFrom";
	private static final String INTERVAL_PREFIX = "CompletableInterval";
	private static final String NEVER = "CompletableNever";
	
	private Function<Completable, Completable>  delegate = null;
	
	public NRCompletableAssembly() {
		
	}
	
	public void setDelegate(Function<Completable, Completable>  d) {
		delegate = d;
	}
	
	public Function<Completable, Completable> getDelegate() {
		return delegate;
	}

	@Override
	public  Completable apply(Completable t) throws Exception {
		if(Utils.useSegments) {
			String simpleName = t.getClass().getSimpleName();
			if(simpleName.startsWith(FROM_PREFIX)) return t;
			if(simpleName.startsWith(INTERVAL_PREFIX)) return t;
			if(simpleName.equals(NEVER)) return t;
			if(Utils.getCompletableIgnores().contains(simpleName)) return t;
			if(t.completableName == null) {
				t.completableName = simpleName;
			}
			NRRxJava2Holder<?> holder = new NRRxJava2Holder<>("Completable",t.completableName, NewRelic.getAgent().getTransaction());
			NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
			NRCompleteAction completeAction = new NRCompleteAction(holder);
			NRTerminationAction terminationAction = new NRTerminationAction(holder);
			NRDisposableConsumer disposableConsumer = new NRDisposableConsumer(holder);
			
			Completable result = delegate != null ? delegate.apply(t) : t;
			
			Completable subscribe = new CompletablePeek(result, disposableConsumer, errorConsumer, completeAction, terminationAction, Functions.EMPTY_ACTION,Functions.EMPTY_ACTION);
			return subscribe;
		}
		return delegate != null ? delegate.apply(t) : t;
	}

}
