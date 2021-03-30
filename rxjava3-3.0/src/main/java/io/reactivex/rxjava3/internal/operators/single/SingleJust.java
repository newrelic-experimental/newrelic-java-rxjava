package io.reactivex.rxjava3.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRSingleObserver;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

@Weave
public class SingleJust<T> extends Single<T> {

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(SingleObserver<? super T> s) {
		if (Utils.useSegments) {
			NRSingleObserver<? super T> wrapper = new NRSingleObserver(s);
			String name = "Single/" + singleName != null ? singleName : "SingleJust";
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			s = wrapper;
		}
		Weaver.callOriginal();
	}

}
