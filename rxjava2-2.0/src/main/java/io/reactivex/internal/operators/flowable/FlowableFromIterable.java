package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRFlowableObserver;
import com.newrelic.instrumentation.rxjava2.Utils;

import io.reactivex.Flowable;

@Weave
public class FlowableFromIterable<T> extends Flowable<T> {

	
	public void subscribeActual(Subscriber<? super T> t) {
		if(Utils.useSegments) {
			String name = "Flowable/" + flowableName != null ? flowableName : getClass().getSimpleName();
			NRFlowableObserver<? super T> wrapper = FlowableUtils.getWrapper(t, name);
			if(wrapper != null) {
				t = wrapper;			
			}
		}
		Weaver.callOriginal();
	}

}
