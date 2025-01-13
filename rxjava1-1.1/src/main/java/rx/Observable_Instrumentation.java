package rx;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.rxjava1.NROperator;

import rx.Observable.Operator;

@Weave(originalName="rx.Observable",type=MatchType.BaseClass)
public abstract class Observable_Instrumentation<T> {


	public Observable<T> observeOn(Scheduler scheduler) {
		Observable<T> result = Weaver.callOriginal();

		Operator<T,T> lifter = new NROperator<T>();
		return result.lift(lifter);
	}

	public Observable<T> subscribeOn(Scheduler scheduler) {
		Observable<T> result = Weaver.callOriginal();

		Operator<T,T> lifter = new NROperator<T>();
		return result.lift(lifter);
	}
}
