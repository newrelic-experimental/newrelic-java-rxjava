package com.newrelic.instrumentation.labs.rxjava3;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.internal.operators.maybe.MaybePeek;

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
	public  Maybe apply(Maybe t) throws Throwable {
		if(Utils.useSegments) {
			if(t.maybeName == null) {
				t.maybeName = t.getClass().getSimpleName();
			}
			NRRxJava3Holder<?> holder = new NRRxJava3Holder<>("Maybe",t.maybeName, NewRelic.getAgent().getTransaction());
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
