package io.reactivex.rxjava3.internal.operators.maybe;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRMaybeObserver;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;

@Weave
public class MaybeFromFuture<T> extends Maybe<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(MaybeObserver<? super T> observer) {
		if(Utils.useSegments) {
			NRMaybeObserver<? super T> wrapper = new NRMaybeObserver(observer);
			String name = "Maybe/" + maybeName != null ? maybeName : getClass().getSimpleName();
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			observer = wrapper;			
		}
		Weaver.callOriginal();
	}

}
