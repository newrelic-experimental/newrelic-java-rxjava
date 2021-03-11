package com.newrelic.instrumentation.rxjava2;

import java.util.logging.Level;

import org.reactivestreams.Subscriber;

import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Config;
import com.newrelic.api.agent.NewRelic;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

public class Utils  implements AgentConfigListener {

	public static boolean initialized = false;
	public static boolean useSegments = true;
	private static final String USESEGMENTS = "RxJava2.useSegments";

	private static final Utils INSTANCE = new Utils();

	private Utils() {

	}

	public static void init() {
		boolean lockdown = RxJavaPlugins.isLockdown();

		if (lockdown) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Unable to add New Relic RxJava2 Wrappers because RxJavaPlugins is locked down");
		} else {
			NRRunnableDecorator decorator;
			Function<Runnable, Runnable> f = RxJavaPlugins.getScheduleHandler();
			decorator = new NRRunnableDecorator(f);
			RxJavaPlugins.setScheduleHandler(decorator);
			ServiceFactory.getConfigService().addIAgentConfigListener(INSTANCE);
			Config config = NewRelic.getAgent().getConfig();
			if (config != null) {
				Boolean b = config.getValue(USESEGMENTS);

				if (b != null && b != useSegments) {
					useSegments = b;
					removeSubscribeDecorators(!useSegments);
				}

			}
			NewRelic.getAgent().getLogger().log(Level.FINE, "Value of useSegments is set to {0}", useSegments);
			addSubscribeDecorators(useSegments);
		}
		initialized = true;
	}

	private static void addSubscribeDecorators(boolean add) {
		boolean lockdown = RxJavaPlugins.isLockdown();
		if(lockdown) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Unable to add New Relic RxJava2 Subscribe descorators because RxJavaPlugins is locked down");
			return;
		}
		if(add) {
			RxJavaPlugins.setOnObservableSubscribe(new NRObservableSubWrapper(null));
			RxJavaPlugins.setOnSingleSubscribe(new NRSingleSubWrapper(null));
			RxJavaPlugins.setOnCompletableSubscribe(new NRCompletableSubWrapper(null));
			RxJavaPlugins.setOnFlowableSubscribe(new NRFlowableSubWrapper(null));
			RxJavaPlugins.setOnMaybeSubscribe(new NRMaybeSubWrapper(null));
		}
	}

	@SuppressWarnings("rawtypes")
	private static void removeSubscribeDecorators(boolean remove) {
		boolean lockdown = RxJavaPlugins.isLockdown();
		if(lockdown) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Unable to remove New Relic RxJava2 Subscribe descorators because RxJavaPlugins is locked down");
			return;
		}
		if(remove) {
			BiFunction<Completable, CompletableObserver, CompletableObserver> fComp = RxJavaPlugins.getOnCompletableSubscribe();
			if(fComp != null) {
				if(fComp instanceof NRCompletableSubWrapper) {
					NRCompletableSubWrapper wrapper = (NRCompletableSubWrapper)fComp;
					BiFunction<Completable, CompletableObserver, CompletableObserver> delegate = wrapper.getDelegate();
					RxJavaPlugins.setOnCompletableSubscribe(delegate);

				}
			}
			BiFunction<Flowable, Subscriber, Subscriber> fFlow = RxJavaPlugins.getOnFlowableSubscribe();
			if(fFlow != null) {
				if(fFlow instanceof NRFlowableSubWrapper) {
					NRFlowableSubWrapper wrapper = (NRFlowableSubWrapper)fFlow;
					BiFunction<Flowable, Subscriber, Subscriber> delegate = wrapper.getDelegate();
					RxJavaPlugins.setOnFlowableSubscribe(delegate);
				}
			}
			BiFunction<Maybe, MaybeObserver, MaybeObserver> fMaybe = RxJavaPlugins.getOnMaybeSubscribe();
			if(fMaybe != null) {
				if(fMaybe instanceof NRMaybeSubWrapper) {
					NRMaybeSubWrapper wrapper = (NRMaybeSubWrapper)fMaybe;
					BiFunction<Maybe, MaybeObserver, MaybeObserver> delegate = wrapper.getDelegate();
					RxJavaPlugins.setOnMaybeSubscribe(delegate);
				}
			}
			BiFunction<Observable, Observer, Observer> fObs = RxJavaPlugins.getOnObservableSubscribe();
			if(fObs != null) {
				if(fObs instanceof NRObservableSubWrapper) {
					NRObservableSubWrapper wrapper = (NRObservableSubWrapper)fObs;
					BiFunction<Observable, Observer, Observer> delegate = wrapper.getDelegate();
					RxJavaPlugins.setOnObservableSubscribe(delegate);
				}
			}
			BiFunction<Single, SingleObserver, SingleObserver> fSingle = RxJavaPlugins.getOnSingleSubscribe();
			if(fSingle != null) {
				if(fSingle instanceof NRSingleSubWrapper) {
					NRSingleSubWrapper wrapper = (NRSingleSubWrapper)fSingle;
					BiFunction<Single, SingleObserver, SingleObserver> delegate = wrapper.getDelegate();
					RxJavaPlugins.setOnSingleSubscribe(delegate);
				}
			}
		}
	}


	@Override
	public void configChanged(String appName, AgentConfig agentConfig) {
		Boolean b = agentConfig.getValue(USESEGMENTS);
		if(b != null && b != useSegments) {
			useSegments = b;
			removeSubscribeDecorators(!useSegments);
			addSubscribeDecorators(useSegments);
		}
		if(useSegments) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Will use segments for RxJava objects");
		} else {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Will not use segments for RxJava objects");
		}
	}

}
