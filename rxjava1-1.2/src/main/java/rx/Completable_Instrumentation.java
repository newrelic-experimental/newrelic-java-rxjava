package rx;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.rxjava1_2.NRCompletableOperator;

import rx.Completable.Operator;

@Weave(originalName="rx.Completable")
public abstract class Completable_Instrumentation {

	public Completable subscribeOn(final Scheduler scheduler) {
		Completable result = Weaver.callOriginal();
		NRCompletableOperator operator = new NRCompletableOperator();
		return result.lift(operator);
	}
	
	public Completable observeOn(final Scheduler scheduler) {
		Completable result = Weaver.callOriginal();
		NRCompletableOperator operator = new NRCompletableOperator();
		return result.lift(operator);
	}
	
	public abstract Completable lift(Operator onLift);
}
