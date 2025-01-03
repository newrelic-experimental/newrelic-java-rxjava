package io.reactivex;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

public class NRSingleWrapper<T> extends Single<T> {
	
	private Single<T> delegate = null;
	private static boolean isTransformed = false;
	
	public NRSingleWrapper(Single<T> d) {
		delegate = d;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	@Trace(dispatcher=true)
	protected void subscribeActual(SingleObserver<? super T> observer) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","SingleWrapper", delegate != null ? delegate.getClass().getSimpleName() : "Null-Single");
		if(delegate != null) {
			delegate.subscribeActual(observer);
		}
	}

}
