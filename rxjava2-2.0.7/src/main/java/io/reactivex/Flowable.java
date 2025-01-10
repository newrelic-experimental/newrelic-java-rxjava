package io.reactivex;

import org.reactivestreams.Publisher;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type=MatchType.BaseClass)
public abstract class Flowable<T> implements Publisher<T> {
	
	@NewField
	public String flowableName = null;
	
}
