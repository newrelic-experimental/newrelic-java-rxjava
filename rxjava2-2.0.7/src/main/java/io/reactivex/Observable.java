package io.reactivex;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type=MatchType.BaseClass)
public abstract class Observable<T> implements ObservableSource<T> {
	
	@NewField
	public String observableName = null;

}
