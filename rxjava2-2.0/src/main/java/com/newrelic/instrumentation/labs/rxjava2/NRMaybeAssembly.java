package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.maybe.MaybePeek;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NRMaybeAssembly implements Function<Maybe, Maybe> {
	
	private Function<Maybe, Maybe>  delegate = null;
	
	public NRMaybeAssembly() {
		
	}
	
	public void setDelegate(Function<Maybe, Maybe>  d) {
		delegate = d;
	}
	
	public Function<Maybe, Maybe> getDelegate() {
		return delegate;
	}

	@Override
	public  Maybe apply(Maybe t) throws Exception {
		if(Utils.useSegments) {
			if(t.maybeName == null) {
				t.maybeName = t.getClass().getSimpleName();
			}
			NRRxJava2Holder<?> holder = new NRRxJava2Holder<>("Maybe",t.maybeName, NewRelic.getAgent().getTransaction());
			NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
			NRCompleteAction completeAction = new NRCompleteAction(holder);
			NRTerminationAction terminationAction = new NRTerminationAction(holder);
			NRDisposableConsumer disposableConsumer = new NRDisposableConsumer(holder);
			NRSuccessConsumer successConsumer = new NRSuccessConsumer<>(holder);
			
			Maybe result = delegate != null ? delegate.apply(t) : t;
			
			Maybe subscribe = new MaybePeek(result, disposableConsumer,successConsumer, errorConsumer, completeAction, terminationAction,Functions.EMPTY_ACTION);
			return subscribe;
		}
		return delegate != null ? delegate.apply(t) : t;
	}

}
