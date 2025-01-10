package io.reactivex;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Weave(type=MatchType.BaseClass)
public abstract class Single<T> implements SingleSource<T> {

	@NewField
	public String singleName = null;
	
	public abstract Single<T> doOnError(final Consumer<? super Throwable> onError) ;
	public abstract Single<T> doOnSubscribe(final Consumer<? super Disposable> onSubscribe);
	public abstract Single<T> doOnSuccess(final Consumer<? super T> onSuccess);
}
