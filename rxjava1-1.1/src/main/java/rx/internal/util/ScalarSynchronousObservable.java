package rx.internal.util;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.rxjava1.NROperator;

import rx.Observable;
import rx.Scheduler;
import rx.Observable.Operator;

@Weave
public abstract class ScalarSynchronousObservable<T> {

	
	public Observable<T> scalarScheduleOn(Scheduler scheduler) {
		Observable<T> result = Weaver.callOriginal();
		
		Operator<T,T> lifter = new NROperator<T>();
		return result.lift(lifter);
	}
}
