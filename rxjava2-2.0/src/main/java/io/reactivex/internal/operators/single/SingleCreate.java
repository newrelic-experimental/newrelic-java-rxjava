package io.reactivex.internal.operators.single;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRSingleObserver;
import com.newrelic.instrumentation.rxjava2.Utils;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

@Weave
public class SingleCreate<T> extends Single<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(SingleObserver<? super T> s) {
		if (Utils.USE_SEGMENTS) {
			NRSingleObserver<? super T> wrapper = new NRSingleObserver(s);
			String name = "Single/" + singleName != null ? singleName : "SingleCreate";
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			s = wrapper;
		}
		Weaver.callOriginal();
	}

}
