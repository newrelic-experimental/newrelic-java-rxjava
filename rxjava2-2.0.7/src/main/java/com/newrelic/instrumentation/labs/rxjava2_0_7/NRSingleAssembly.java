package com.newrelic.instrumentation.labs.rxjava2_0_7;

import com.newrelic.api.agent.NewRelic;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.single.SingleDoOnError;
import io.reactivex.internal.operators.single.SingleDoOnSubscribe;
import io.reactivex.internal.operators.single.SingleDoOnSuccess;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NRSingleAssembly implements Function<Single, Single> {
	
	private Function<Single, Single>  delegate = null;
	
	public NRSingleAssembly() {
		
	}
	
	public void setDelegate(Function<Single, Single>  d) {
		delegate = d;
	}
	
	public Function<Single, Single> getDelegate() {
		return delegate;
	}

	@Override
	public  Single apply(Single t) throws Exception {
		if(Utils.useSegments) {
			if(t.singleName == null) {
				t.singleName = t.getClass().getSimpleName();
			}
			NRRxJava2Holder<?> holder = new NRRxJava2Holder<>("Single",t.singleName, NewRelic.getAgent().getTransaction());
			NRErrorConsumer errorConsumer = new NRErrorConsumer(holder);
			NRDisposableConsumer disposeConsumer = new NRDisposableConsumer(holder);
			NRSuccessConsumer sucessConsumer = new NRSuccessConsumer<>(holder);
			Single result = delegate != null ? delegate.apply(t) : t;
			Single error = new SingleDoOnError<>(result, errorConsumer);
			Single success = new SingleDoOnSuccess<>(error, sucessConsumer);
			Single subscribe = new SingleDoOnSubscribe<>(success, disposeConsumer);

			return subscribe;
		}
		return delegate != null ? delegate.apply(t) : t;
	}

}
