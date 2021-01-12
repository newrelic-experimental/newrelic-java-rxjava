package com.newrelic.instrumentation.rxjava2;

import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

public class Utils {

	public static boolean initialized = false;


	public static void init() {
		NRRunnableDecorator decorator;

		Function<Runnable,Runnable> f = RxJavaPlugins.getScheduleHandler();
		decorator = new NRRunnableDecorator(f);

		RxJavaPlugins.setScheduleHandler(decorator);
		initialized = true;
	}

}
