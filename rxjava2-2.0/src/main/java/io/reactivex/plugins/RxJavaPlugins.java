package io.reactivex.plugins;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.rxjava2.NRRunnableDecorator;
import com.newrelic.instrumentation.rxjava2.Utils;

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
}
