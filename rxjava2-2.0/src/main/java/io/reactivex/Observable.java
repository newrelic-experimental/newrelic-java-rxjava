package io.reactivex;

import java.util.concurrent.Callable;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Weave(type=MatchType.BaseClass)
public abstract class Observable<T> {

	@NewField
	public String observableName = null;

	// Included in order to run tests
	public static <T> Observable<T> fromCallable(Callable<? extends T> supplier) {
		return Weaver.callOriginal();
	}

	
	public abstract Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
            Action onComplete);
}
