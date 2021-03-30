package io.reactivex.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRObservableObserver;
import com.newrelic.instrumentation.rxjava2.Utils;

import io.reactivex.Observable;
import io.reactivex.Observer;

@Weave
public abstract class ObservableDefer<T> extends Observable<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribeActual(Observer<? super T> observer) {
		if (Utils.useSegments) {
			NRObservableObserver<? super T> wrapper = new NRObservableObserver(observer);
			String name = "Observable/" + observableName != null ? observableName : "ObservableDefer";
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			observer = wrapper;
		}
		
		Weaver.callOriginal();
	}
}
