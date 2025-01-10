package com.newrelic.instrumentation.labs.rxjava2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
	public static boolean useDecorators = true;
	private static final String USESEGMENTS = "RxJava2.useSegments";
	private static final String USEDECORATORS = "RxJava2.useDecorators";
	private static final String COMPLETABLE_IGNORES = "RxJava2.Completable.ignores";
	private static final String FLOWABLE_IGNORES = "RxJava2.Flowable.ignores";
	private static final String MAYBE_IGNORES = "RxJava2.Maybe.ignores";
	private static final String OBSERVABLE_IGNORES = "RxJava2.Observable.ignores";
	private static final String SINGLE_IGNORES = "RxJava2.Single.ignores";
	private static Set<String> completable_ignores = new HashSet<>();
	private static Set<String> flowable_ignores = new HashSet<>();
	private static Set<String> maybe_ignores = new HashSet<>();
	private static Set<String> observable_ignores = new HashSet<>();
	private static Set<String> single_ignores = new HashSet<>();

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
					removeAssemblyDecorators(useSegments);
				}

				b = config.getValue(USEDECORATORS);
				if(b != null && b != useDecorators) {
					useDecorators = b;
					removeSubscribeDecorators(useDecorators);
				}
			}
			NewRelic.getAgent().getLogger().log(Level.FINE, "Value of useSegments is set to {0}", useSegments);
			NewRelic.getAgent().getLogger().log(Level.FINE, "Value of useDecorators is set to {0}", useDecorators);
			addSubscribeDecorators(useDecorators);
			addAssemblyDecorators(useSegments);
			
			String ignores = config.getValue(COMPLETABLE_IGNORES);
			completable_ignores.clear();
			if(ignores != null && !ignores.isEmpty()) {
				String[] splits = ignores.split(",");
				completable_ignores.addAll(Arrays.asList(splits));
				NewRelic.getAgent().getLogger().log(Level.FINE, "Will ignore completables with these names: {0}", completable_ignores);
			}
			ignores = config.getValue(FLOWABLE_IGNORES);
			flowable_ignores.clear();
			if(ignores != null && !ignores.isEmpty()) {
				String[] splits = ignores.split(",");
				flowable_ignores.addAll(Arrays.asList(splits));
				NewRelic.getAgent().getLogger().log(Level.FINE, "Will ignore flowables with these names: {0}", flowable_ignores);
			}
			ignores = config.getValue(MAYBE_IGNORES);
			maybe_ignores.clear();
			if(ignores != null && !ignores.isEmpty()) {
				String[] splits = ignores.split(",");
				maybe_ignores.addAll(Arrays.asList(splits));
				NewRelic.getAgent().getLogger().log(Level.FINE, "Will ignore maybes with these names: {0}", maybe_ignores);
			}
			ignores = config.getValue(OBSERVABLE_IGNORES);
			observable_ignores.clear();
			if(ignores != null && !ignores.isEmpty()) {
				String[] splits = ignores.split(",");
				observable_ignores.addAll(Arrays.asList(splits));
				NewRelic.getAgent().getLogger().log(Level.FINE, "Will ignore observables with these names: {0}", observable_ignores);
			}
			ignores = config.getValue(SINGLE_IGNORES);
			single_ignores.clear();
			if(ignores != null && !ignores.isEmpty()) {
				String[] splits = ignores.split(",");
				single_ignores.addAll(Arrays.asList(splits));
				NewRelic.getAgent().getLogger().log(Level.FINE, "Will ignore singles with these names: {0}", single_ignores);
			}

			
		}
		initialized = true;
	}
	
	public static Set<String> getCompletableIgnores() {
		return completable_ignores;
	}

	public static Set<String> getFlowableIgnores() {
		return flowable_ignores;
	}

	public static Set<String> getMaybeIgnores() {
		return maybe_ignores;
	}

	public static Set<String> getObservableIgnores() {
		return observable_ignores;
	}

	public static Set<String> getSingleIgnores() {
		return single_ignores;
	}

	private static void addAssemblyDecorators(boolean add) {
		boolean lockdown = RxJavaPlugins.isLockdown();
		if(lockdown) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Unable to add New Relic RxJava2 Subscribe descorators because RxJavaPlugins is locked down");
			return;
		}
		if(add) {
			RxJavaPlugins.setOnSingleAssembly(new NRSingleAssembly());
			RxJavaPlugins.setOnFlowableAssembly(new NRFlowableAssembly());
			RxJavaPlugins.setOnCompletableAssembly(new NRCompletableAssembly());
			RxJavaPlugins.setOnMaybeAssembly(new NRMaybeAssembly());
			RxJavaPlugins.setOnObservableAssembly(new NRObservableAssembly());
		}
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
	public static void removeAssemblyDecorators(boolean remove) {
		boolean lockdown = RxJavaPlugins.isLockdown();
		if(lockdown) {
			NewRelic.getAgent().getLogger().log(Level.INFO, "Unable to remove New Relic RxJava2 Assembly descorators because RxJavaPlugins is locked down");
			return;
		}
		if(remove) {
			Function<Single, Single> fSingle = RxJavaPlugins.getOnSingleAssembly();
			if(fSingle != null) {
				if(fSingle instanceof NRSingleAssembly) {
					NRSingleAssembly singleAssembly = (NRSingleAssembly)fSingle;
					Function<Single, Single> delegate = singleAssembly.getDelegate();
					RxJavaPlugins.setOnSingleAssembly(delegate);
				}
			}
			Function<Completable,Completable> fCompletable = RxJavaPlugins.getOnCompletableAssembly();
			if(fCompletable != null) {
				if(fCompletable instanceof NRCompletableAssembly) {
					NRCompletableAssembly nrAssembly = (NRCompletableAssembly)fCompletable;
					Function<Completable,Completable>  delegate = nrAssembly.getDelegate();
					RxJavaPlugins.setOnCompletableAssembly(delegate);
				}
			}
			Function<Flowable,Flowable> fFlowable = RxJavaPlugins.getOnFlowableAssembly();
			if(fFlowable != null) {
				if(fFlowable instanceof NRFlowableAssembly) {
					NRFlowableAssembly nrAssembly = (NRFlowableAssembly)fFlowable;
					Function<Flowable,Flowable>  delegate = nrAssembly.getDelegate();
					RxJavaPlugins.setOnFlowableAssembly(delegate);
				}
			}
			Function<Maybe,Maybe> fMaybe = RxJavaPlugins.getOnMaybeAssembly();
			if(fMaybe != null) {
				if(fMaybe instanceof NRMaybeAssembly) {
					NRMaybeAssembly nrAssembly = (NRMaybeAssembly)fMaybe;
					Function<Maybe, Maybe> delegate = nrAssembly.getDelegate();
					RxJavaPlugins.setOnMaybeAssembly(delegate);
				}
			}
			Function<Observable,Observable> fObservable = RxJavaPlugins.getOnObservableAssembly();
			if(fObservable != null) {
				if(fObservable instanceof NRObservableAssembly) {
					NRObservableAssembly nrAssembly = (NRObservableAssembly)fObservable;
					Function<Observable,Observable> delegate = nrAssembly.getDelegate();
					RxJavaPlugins.setOnObservableAssembly(delegate);
				}
			}
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
		String ignores = agentConfig.getValue(COMPLETABLE_IGNORES);
		completable_ignores.clear();
		if(ignores != null && !ignores.isEmpty()) {
			String[] splits = ignores.split(",");
			completable_ignores.addAll(Arrays.asList(splits));
		}
		ignores = agentConfig.getValue(FLOWABLE_IGNORES);
		flowable_ignores.clear();
		if(ignores != null && !ignores.isEmpty()) {
			String[] splits = ignores.split(",");
			flowable_ignores.addAll(Arrays.asList(splits));
		}
		ignores = agentConfig.getValue(MAYBE_IGNORES);
		maybe_ignores.clear();
		if(ignores != null && !ignores.isEmpty()) {
			String[] splits = ignores.split(",");
			maybe_ignores.addAll(Arrays.asList(splits));
		}
		ignores = agentConfig.getValue(OBSERVABLE_IGNORES);
		observable_ignores.clear();
		if(ignores != null && !ignores.isEmpty()) {
			String[] splits = ignores.split(",");
			observable_ignores.addAll(Arrays.asList(splits));
		}
		ignores = agentConfig.getValue(SINGLE_IGNORES);
		single_ignores.clear();
		if(ignores != null && !ignores.isEmpty()) {
			String[] splits = ignores.split(",");
			single_ignores.addAll(Arrays.asList(splits));
		}
		
	}

}
