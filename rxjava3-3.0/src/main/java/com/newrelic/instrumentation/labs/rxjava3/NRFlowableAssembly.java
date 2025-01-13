package com.newrelic.instrumentation.labs.rxjava3;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableDoOnEach;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableDoOnLifecycle;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NRFlowableAssembly implements Function<Flowable, Flowable> {
	
	private static final String FROM_PREFIX = "FlowableFrom";
	private static final String INTERVAL_PREFIX = "FlowableInterval";
	private static final String NEVER = "FlowableNever";

	private Function<Flowable, Flowable>  delegate = null;
	
	public NRFlowableAssembly() {
		
	}
	
	public void setDelegate(Function<Flowable, Flowable>  d) {
		delegate = d;
	}
	
	public Function<Flowable, Flowable> getDelegate() {
		return delegate;
	}

	@Override
	public  Flowable apply(Flowable t) throws Throwable {
		if(Utils.useSegments) {
			String simpleName = t.getClass().getSimpleName();
			if(simpleName.startsWith(FROM_PREFIX)) return t;
			if(simpleName.startsWith(INTERVAL_PREFIX)) return t;
			if(simpleName.equals(NEVER)) return t;
			if(Utils.getFlowableIgnores().contains(simpleName)) return t;
			if(t.flowableName == null) {
				t.flowableName = simpleName;
			}
			NRRxJava3Holder<?> holder = new NRRxJava3Holder<>("Flowable",t.flowableName, NewRelic.getAgent().getTransaction());
			NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
			NRCompleteAction completeAction = new NRCompleteAction(holder);
			NRNextConsumer nextConsumer = new NRNextConsumer<>(holder);
			NRTerminationAction terminationAction = new NRTerminationAction(holder);
			NRSubscriberConsumer subscribeConsumer = new NRSubscriberConsumer(holder);
			NRCancelAction cancelAction = new NRCancelAction(holder);
			
			
			Flowable result = delegate != null ? delegate.apply(t) : t;
			Flowable result2 = new FlowableDoOnEach<>(result, nextConsumer , errorConsumer, completeAction, terminationAction);
			Flowable subscribe = new FlowableDoOnLifecycle<>(result2, subscribeConsumer, Functions.EMPTY_LONG_CONSUMER, cancelAction);
			return subscribe;
		}
		return delegate != null ? delegate.apply(t) : t;
	}

}
