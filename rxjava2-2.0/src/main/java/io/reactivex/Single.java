package io.reactivex;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type=MatchType.BaseClass)
public abstract class Single<T> {

	@NewField
	public String singleName = null;
	
	 protected abstract void subscribeActual(SingleObserver<? super T> observer);
}
