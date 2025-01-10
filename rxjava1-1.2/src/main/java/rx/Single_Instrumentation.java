package rx;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.rxjava1_2.NRSingleOperator;

import rx.Observable.Operator;

@Weave(originalName="rx.Single")
public abstract class Single_Instrumentation<T> {

	public Single<T> subscribeOn(final Scheduler scheduler) {
		Single<T> result = Weaver.callOriginal();
		NRSingleOperator<T> operator = new NRSingleOperator<T>();
		return result.lift(operator);
	}
	
	public Single<T> observeOn(Scheduler scheduler) {
		Single<T> result = Weaver.callOriginal();
		NRSingleOperator<T> operator = new NRSingleOperator<T>();
		return result.lift(operator);
	}
	
	public abstract <R> Single<R> lift(final Operator<? extends R, ? super T> lift);
}
