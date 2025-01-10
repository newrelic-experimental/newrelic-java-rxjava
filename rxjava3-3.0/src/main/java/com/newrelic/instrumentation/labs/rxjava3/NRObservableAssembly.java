package com.newrelic.instrumentation.labs.rxjava3;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.internal.operators.observable.ObservableDoOnEach;
import io.reactivex.rxjava3.internal.operators.observable.ObservableDoOnLifecycle;

@SuppressWarnings({"rawtypes","unchecked"})
public class NRObservableAssembly implements Function<Observable, Observable> {
	
	private Function<Observable, Observable>  delegate = null;
	private static final String FROM_PREFIX = "ObservableFrom";
	private static final String INTERVAL_PREFIX = "ObservableInterval";
	private static final String NEVER = "ObservableNever";

	public NRObservableAssembly() {
		
	}
	
	public void setDelegate(Function<Observable, Observable>  d) {
		delegate = d;
	}
	
	public Function<Observable, Observable> getDelegate() {
		return delegate;
	}

	@Override
	public  Observable apply(Observable t) throws Throwable {
		if(Utils.useSegments) {
			String simpleName = t.getClass().getSimpleName();
			if(simpleName.startsWith(FROM_PREFIX)) return t;
			if(simpleName.startsWith(INTERVAL_PREFIX)) return t;
			if(simpleName.equals(NEVER)) return t;
			if(Utils.getObservableIgnores().contains(simpleName)) return t;
			
			if(t.observableName == null) {
				t.observableName = simpleName;
			}
			NRRxJava3Holder<?> holder = new NRRxJava3Holder<>("Observable",t.observableName, NewRelic.getAgent().getTransaction());
			NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
			NRCompleteAction completeAction = new NRCompleteAction(holder);
			NRTerminationAction terminationAction = new NRTerminationAction(holder);
			NRNextConsumer nextConsumer = new NRNextConsumer<>(holder);
			NRDisposableConsumer disposableConsumer = new NRDisposableConsumer(holder);
			Observable result = delegate != null ? delegate.apply(t) : t;
			
			Observable subscribe = new ObservableDoOnLifecycle<>(result, disposableConsumer, Functions.EMPTY_ACTION);
			Observable result2 = new ObservableDoOnEach(subscribe, nextConsumer, errorConsumer, completeAction, terminationAction);
			return result2;
		}
		return delegate != null ? delegate.apply(t) : t;
	}

}
