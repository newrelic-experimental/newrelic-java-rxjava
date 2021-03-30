package io.reactivex.rxjava3.internal.operators.observable;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRObservableObserver;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

@Weave
public abstract class ObservableCreate<T> extends Observable<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void subscribeActual(Observer<? super T> observer) {
		if (Utils.useSegments) {
			NRObservableObserver<? super T> wrapper = new NRObservableObserver(observer);
			String name = "Observable/" + observableName != null ? observableName : "ObservableCreate";
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(name);
			wrapper.segment = segment;
			observer = wrapper;
		}
		
		Weaver.callOriginal();
	}
}
