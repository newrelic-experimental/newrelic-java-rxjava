package io.reactivex.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRCompletableObserver;
import com.newrelic.instrumentation.rxjava2.Utils;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

@Weave
public class CompletableError extends Completable {

	protected void subscribeActual(CompletableObserver s) {
		if(Utils.USE_SEGMENTS) {
			NRCompletableObserver wrapper = new NRCompletableObserver(s);
			String name = "Completable/" + completableName != null ? completableName : getClass().getSimpleName();
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			s = wrapper;			
		}
		Weaver.callOriginal();
	}
}
