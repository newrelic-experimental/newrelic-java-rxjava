package io.reactivex.rxjava3.plugins;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava3.NRCompletableSubWrapper;
import com.newrelic.instrumentation.rxjava3.NRFlowableSubWrapper;
import com.newrelic.instrumentation.rxjava3.NRMaybeSubWrapper;
import com.newrelic.instrumentation.rxjava3.NRObservableSubWrapper;
import com.newrelic.instrumentation.rxjava3.NRRunnableDecorator;
import com.newrelic.instrumentation.rxjava3.NRSingleSubWrapper;
import com.newrelic.instrumentation.rxjava3.Utils;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.NRSingleWrapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.completable.CompletableUtils;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableUtils;
import io.reactivex.rxjava3.internal.operators.maybe.MaybeUtils;
import io.reactivex.rxjava3.internal.operators.observable.ObervableUtils;

@SuppressWarnings("rawtypes")
@Weave
public abstract class RxJavaPlugins {


	static volatile Function<Runnable, Runnable> onScheduleHandler = Weaver.callOriginal();

	static volatile BiFunction<Observable, Observer, Observer> onObservableSubscribe = Weaver.callOriginal();

	static volatile BiFunction<Single, SingleObserver, SingleObserver> onSingleSubscribe = Weaver.callOriginal();

	static volatile BiFunction<Completable, CompletableObserver, CompletableObserver> onCompletableSubscribe = Weaver.callOriginal();

	static volatile BiFunction<Flowable, Subscriber, Subscriber> onFlowableSubscribe = Weaver.callOriginal();

	static volatile BiFunction<Maybe, MaybeObserver, MaybeObserver> onMaybeSubscribe = Weaver.callOriginal();
	
	public static boolean isLockdown() {
		return Weaver.callOriginal();
	}

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

	public static BiFunction<Completable, CompletableObserver, CompletableObserver> getOnCompletableSubscribe() {
		return Weaver.callOriginal();
	}

	public static BiFunction<Flowable, Subscriber, Subscriber> getOnFlowableSubscribe() {
		return Weaver.callOriginal();
	}

	public static BiFunction<Maybe, MaybeObserver, MaybeObserver> getOnMaybeSubscribe() {
		return Weaver.callOriginal();
	}
	
	public static BiFunction<Observable, Observer, Observer> getOnObservableSubscribe() {
		return Weaver.callOriginal();
	}
	
	public static BiFunction<Single, SingleObserver, SingleObserver> getOnSingleSubscribe() {
		return Weaver.callOriginal();
	}
	
	public static void setOnMaybeSubscribe(BiFunction<Maybe, MaybeObserver, MaybeObserver> onMaybeSubscribe1) {
		if (Utils.useSegments) {
			// only have to manipulate if there is an existing function set
			if (onMaybeSubscribe != null) {
				if (onMaybeSubscribe instanceof NRMaybeSubWrapper) {
					NRMaybeSubWrapper nr2_1 = (NRMaybeSubWrapper) onMaybeSubscribe;
					if (onMaybeSubscribe1 instanceof NRMaybeSubWrapper) {
						// if incoming and existing are NR then decide which delegate to use
						NRMaybeSubWrapper nr2_2 = (NRMaybeSubWrapper) onMaybeSubscribe1;
						BiFunction<Maybe, MaybeObserver, MaybeObserver> d1 = nr2_1.getDelegate();
						BiFunction<Maybe, MaybeObserver, MaybeObserver> d2 = nr2_2.getDelegate();
						if (d2 == null && d1 != null) {
							nr2_2.setDelegate(d1);
						}

					} else {
						nr2_1.setDelegate(onMaybeSubscribe1);
						onMaybeSubscribe1 = nr2_1;
					}
				} else {
					if (onMaybeSubscribe1 instanceof NRMaybeSubWrapper) {
						NRMaybeSubWrapper nr2_1 = (NRMaybeSubWrapper) onMaybeSubscribe1;
						nr2_1.setDelegate(onMaybeSubscribe);
					}

				}
			} else {
				// if not NR then wrap the incoming
				if (!(onMaybeSubscribe1 instanceof NRMaybeSubWrapper)) {
					NRMaybeSubWrapper nr = new NRMaybeSubWrapper(onMaybeSubscribe1);
					onMaybeSubscribe1 = nr;
				}

			} 
		}
		Weaver.callOriginal();
	}

	public static void setOnObservableSubscribe(BiFunction<Observable, Observer, Observer> onObservableSubscribe1) {
		if (Utils.useSegments) {
			// only have to manipulate if there is an existing function set
			if(onObservableSubscribe != null) {
				if(onObservableSubscribe instanceof NRObservableSubWrapper) {
					NRObservableSubWrapper nr2_1 = (NRObservableSubWrapper)onObservableSubscribe;
					if(onObservableSubscribe1 instanceof NRObservableSubWrapper) {
						// if incoming and existing are NR then decide which delegate to use
						NRObservableSubWrapper nr2_2 = (NRObservableSubWrapper)onObservableSubscribe1;
						BiFunction<Observable, Observer, Observer> d1 = nr2_1.getDelegate();
						BiFunction<Observable, Observer, Observer> d2 = nr2_2.getDelegate();
						if(d2 == null && d1 != null) {
							nr2_2.setDelegate(d1);
						}

					} else {
						nr2_1.setDelegate(onObservableSubscribe1);
						onObservableSubscribe1 = nr2_1;
					}
				} else {
					if(onObservableSubscribe1 instanceof NRObservableSubWrapper) {
						NRObservableSubWrapper nr2_1 = (NRObservableSubWrapper)onObservableSubscribe1;
						nr2_1.setDelegate(onObservableSubscribe);
					}

				}
			} else {
				// if not NR then wrap the incoming
				if(!(onObservableSubscribe1 instanceof NRObservableSubWrapper)) {
					NRObservableSubWrapper nr = new NRObservableSubWrapper(onObservableSubscribe1);
					onObservableSubscribe1 = nr;
				}

			}
		}
		Weaver.callOriginal();
	}

	public static void setOnSingleSubscribe(BiFunction<Single, SingleObserver, SingleObserver> onSingleSubscribe1) {
		if (Utils.useSegments) {
			// only have to manipulate if there is an existing function set
			if(onSingleSubscribe != null) {
				if(onSingleSubscribe instanceof NRSingleSubWrapper) {
					NRSingleSubWrapper nr2_1 = (NRSingleSubWrapper)onSingleSubscribe;
					if(onSingleSubscribe1 instanceof NRSingleSubWrapper) {
						// if incoming and existing are NR then decide which delegate to use
						NRSingleSubWrapper nr2_2 = (NRSingleSubWrapper)onSingleSubscribe1;
						BiFunction<Single, SingleObserver, SingleObserver> d1 = nr2_1.getDelegate();
						BiFunction<Single, SingleObserver, SingleObserver> d2 = nr2_2.getDelegate();
						if(d2 == null && d1 != null) {
							nr2_2.setDelegate(d1);
						}

					} else {
						nr2_1.setDelegate(onSingleSubscribe1);
						onSingleSubscribe1 = nr2_1;
					}
				} else {
					if(onSingleSubscribe1 instanceof NRSingleSubWrapper) {
						NRSingleSubWrapper nr2_1 = (NRSingleSubWrapper)onSingleSubscribe1;
						nr2_1.setDelegate(onSingleSubscribe);
					}

				}
			} else {
				// if not NR then wrap the incoming
				if(!(onSingleSubscribe1 instanceof NRSingleSubWrapper)) {
					NRSingleSubWrapper nr = new NRSingleSubWrapper(onSingleSubscribe1);
					onSingleSubscribe1 = nr;
				}

			}
		}
		Weaver.callOriginal();

	}

	public static void setOnCompletableSubscribe(BiFunction<Completable, CompletableObserver, CompletableObserver> onCompletableSubscribe1) {
		if (Utils.useSegments) {
			// only have to manipulate if there is an existing function set
			if(onCompletableSubscribe != null) {
				if(onCompletableSubscribe instanceof NRCompletableSubWrapper) {
					NRCompletableSubWrapper nr2_1 = (NRCompletableSubWrapper)onCompletableSubscribe;
					if(onCompletableSubscribe1 instanceof NRCompletableSubWrapper) {
						// if incoming and existing are NR then decide which delegate to use
						NRCompletableSubWrapper nr2_2 = (NRCompletableSubWrapper)onCompletableSubscribe1;
						BiFunction<Completable, CompletableObserver, CompletableObserver> d1 = nr2_1.getDelegate();
						BiFunction<Completable, CompletableObserver, CompletableObserver> d2 = nr2_2.getDelegate();
						if(d2 == null && d1 != null) {
							nr2_2.setDelegate(d1);
						}

					} else {
						nr2_1.setDelegate(onCompletableSubscribe1);
						onCompletableSubscribe1 = nr2_1;
					}
				} else {
					if(onCompletableSubscribe1 instanceof NRCompletableSubWrapper) {
						NRCompletableSubWrapper nr2_1 = (NRCompletableSubWrapper)onCompletableSubscribe1;
						nr2_1.setDelegate(onCompletableSubscribe);
					}

				}
			} else {
				// if not NR then wrap the incoming
				if(!(onCompletableSubscribe1 instanceof NRCompletableSubWrapper)) {
					NRCompletableSubWrapper nr = new NRCompletableSubWrapper(onCompletableSubscribe1);
					onCompletableSubscribe1 = nr;
				}

			}
		}
		Weaver.callOriginal();
	}

	public static void setOnFlowableSubscribe(BiFunction<Flowable, Subscriber, Subscriber> onFlowableSubscribe1) {
		if (Utils.useSegments) {
			// only have to manipulate if there is an existing function set
			if(onFlowableSubscribe != null) {
				if(onFlowableSubscribe instanceof NRFlowableSubWrapper) {
					NRFlowableSubWrapper nr2_1 = (NRFlowableSubWrapper)onFlowableSubscribe;
					if(onFlowableSubscribe1 instanceof NRFlowableSubWrapper) {
						// if incoming and existing are NR then decide which delegate to use
						NRFlowableSubWrapper nr2_2 = (NRFlowableSubWrapper)onFlowableSubscribe1;
						BiFunction<Flowable, Subscriber, Subscriber> d1 = nr2_1.getDelegate();
						BiFunction<Flowable, Subscriber, Subscriber> d2 = nr2_2.getDelegate();
						if(d2 == null && d1 != null) {
							nr2_2.setDelegate(d1);
						}

					} else {
						nr2_1.setDelegate(onFlowableSubscribe1);
						onFlowableSubscribe1 = nr2_1;
					}
				} else {
					if(onFlowableSubscribe1 instanceof NRFlowableSubWrapper) {
						NRFlowableSubWrapper nr2_1 = (NRFlowableSubWrapper)onFlowableSubscribe1;
						nr2_1.setDelegate(onFlowableSubscribe);
					}

				}
			} else {
				// if not NR then wrap the incoming
				if(!(onFlowableSubscribe1 instanceof NRFlowableSubWrapper)) {
					NRFlowableSubWrapper nr = new NRFlowableSubWrapper(onFlowableSubscribe1);
					onFlowableSubscribe1 = nr;
				}

			}
		}		
		Weaver.callOriginal();
	}

	public static <T> Observable<T> onAssembly(Observable<T> source) {
		if(!Utils.initialized) {
			Utils.init();
		}
		Observable<T> observable = Weaver.callOriginal();
		if(!ObervableUtils.ignore(observable) && observable.observableName == null) {
			observable.observableName = NewRelic.getAgent().getTracedMethod().getMetricName();	 
		}

		return observable;
	}

	public static <T> Single<T> onAssembly(Single<T> source) {
		if(!Utils.initialized) {
			Utils.init();
		}
		Single<T> single = Weaver.callOriginal();
		if(single.singleName == null) {
			single.singleName = NewRelic.getAgent().getTracedMethod().getMetricName();
		}

		return new NRSingleWrapper<T>(single);
	}

	public static Completable onAssembly(Completable source) {
		if(!Utils.initialized) {
			Utils.init();
		}
		Completable completable = Weaver.callOriginal();
		if(!CompletableUtils.ignore(completable) && completable.completableName == null) {
			completable.completableName = NewRelic.getAgent().getTracedMethod().getMetricName();
		}

		return completable;
	}

	public static <T> Flowable<T> onAssembly(Flowable<T> source) {
		if(!Utils.initialized) {
			Utils.init();
		}
		Flowable<T> flowable = Weaver.callOriginal();
		if(!FlowableUtils.ignore(flowable) && flowable.flowableName == null) {
			flowable.flowableName = NewRelic.getAgent().getTracedMethod().getMetricName();
		}
		return flowable;
	}

	public static <T> Maybe<T> onAssembly(Maybe<T> source) {
		if(!Utils.initialized) {
			Utils.init();
		}
		Maybe<T> maybe = Weaver.callOriginal();
		if(!MaybeUtils.ignore(maybe) && maybe.maybeName == null) {
			maybe.maybeName = NewRelic.getAgent().getTracedMethod().getMetricName();
		}

		return maybe;
	}
}
