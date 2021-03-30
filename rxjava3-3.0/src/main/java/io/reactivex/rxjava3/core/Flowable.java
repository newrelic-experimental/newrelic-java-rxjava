package io.reactivex.rxjava3.core;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type=MatchType.BaseClass)
public abstract class Flowable<T> {
	
	@NewField
	public String flowableName = null;

}
