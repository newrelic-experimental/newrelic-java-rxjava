package io.reactivex.plugins;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRRunnableDecorator;
import com.newrelic.instrumentation.rxjava2.Utils;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Weave
public abstract class RxJavaPlugins {

	
    static volatile Function<Runnable, Runnable> onScheduleHandler = Weaver.callOriginal();
    
    public static Runnable onSchedule(Runnable run) {
    	if(!Utils.initialized) {
    		Utils.init();
    	}
    	return Weaver.callOriginal();
    }
    
    public static void setScheduleHandler(Function<Runnable, Runnable> handler) {
    	if(!(handler instanceof NRRunnableDecorator)) {
    		NRRunnableDecorator decorator = new NRRunnableDecorator(handler);
    		handler = decorator;
    		if(!Utils.initialized) Utils.initialized = true;
    	}
    	Weaver.callOriginal();
    }

    public static Function<Runnable, Runnable> getScheduleHandler() {
    	return Weaver.callOriginal();
    }
    
    public static <T> Single<T> onAssembly(Single<T> source) {
    	Single<T> single = Weaver.callOriginal();
 
    	String mName = NewRelic.getAgent().getTracedMethod().getMetricName();
    	single.singleName = mName != null && !mName.isEmpty() ? mName : single.getClass().getSimpleName();
    	return single;
    }
    
    public static <T> Observable<T> onAssembly(Observable<T> source) {
    	Observable<T> observable = Weaver.callOriginal();
    	String mName = NewRelic.getAgent().getTracedMethod().getMetricName();
    	observable.observableName = mName != null && !mName.isEmpty() ? mName : observable.getClass().getSimpleName();
    	return observable;
    }
    
    public static <T> Maybe<T> onAssembly(Maybe<T> source) {
    	Maybe<T> maybe = Weaver.callOriginal();
    	String mName = NewRelic.getAgent().getTracedMethod().getMetricName();
    	maybe.maybeName = mName != null && !mName.isEmpty() ? mName : maybe.getClass().getSimpleName();
    	return maybe;
    }
    
    public static <T> Flowable<T> onAssembly(Flowable<T> source) {
    	Flowable<T> flowable = Weaver.callOriginal();
    	String mName = NewRelic.getAgent().getTracedMethod().getMetricName();
    	flowable.flowableName = mName != null && !mName.isEmpty() ? mName : flowable.getClass().getSimpleName();
    	return flowable;
    }

    public static <T> Completable onAssembly(Completable source) {
    	Completable completable = Weaver.callOriginal();
    	String mName = NewRelic.getAgent().getTracedMethod().getMetricName();
    	completable.completableName = mName != null && !mName.isEmpty() ? mName : completable.getClass().getSimpleName();
    	return completable;
    }

}
