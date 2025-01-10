package rx.internal.util;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.rxjava1.NROperator;

import rx.Observable;
import rx.Observable.Operator;
import rx.Scheduler;

@Weave
public abstract class ScalarSynchronousObservable<T> {


	public Observable<T> scalarScheduleOn(Scheduler scheduler) {
		Observable<T> result = Weaver.callOriginal();

		Operator<T,T> lifter = new NROperator<T>();
		return result.lift(lifter);
	}
}
