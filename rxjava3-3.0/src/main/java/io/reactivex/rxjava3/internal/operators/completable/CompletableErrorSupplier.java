package io.reactivex.rxjava3.internal.operators.completable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRCompletableObserver;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

@Weave
public class CompletableErrorSupplier extends Completable {

	protected void subscribeActual(CompletableObserver s) {
		if(Utils.useSegments) {
			NRCompletableObserver wrapper = new NRCompletableObserver(s);
			String name = "Completable/" + completableName != null ? completableName : getClass().getSimpleName();
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			s = wrapper;			
		}
		Weaver.callOriginal();
	}
}
