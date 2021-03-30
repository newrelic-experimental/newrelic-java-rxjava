package io.reactivex.rxjava3.internal.operators.flowable;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRFlowableObserver;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Flowable;

@Weave
public class FlowableRange<T> extends Flowable<T> {

	
	public void subscribeActual(Subscriber<? super T> t) {
		if(Utils.useSegments) {
			NRFlowableObserver<? super T> wrapper = new NRFlowableObserver<T>(t);
			String name = "Flowable/" + flowableName != null ? flowableName : getClass().getSimpleName();
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			t = wrapper;			

		}
		Weaver.callOriginal();
	}

}
