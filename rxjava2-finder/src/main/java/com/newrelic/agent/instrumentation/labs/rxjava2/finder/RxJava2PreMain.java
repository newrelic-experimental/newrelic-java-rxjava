package com.newrelic.agent.instrumentation.labs.rxjava2.finder;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;

import com.newrelic.agent.InstrumentationProxy;
import com.newrelic.agent.TracerService;
import com.newrelic.agent.core.CoreService;
import com.newrelic.agent.instrumentation.ClassTransformerService;
import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.context.InstrumentationContextManager;
import com.newrelic.agent.service.ServiceFactory;

public class RxJava2PreMain {
	
	protected static final String TRACER_FACTORY = "RXJava2_Returning";

	public static void premain(String s, Instrumentation inst) {
		
	}
	
	public static void initialize() {
		boolean done = setup();
		if(!done) {
			Executors.newSingleThreadExecutor().submit(new SetupProcess());
		}
	}
	
	private static boolean setup() {
		TracerService tracerService = ServiceFactory.getTracerService();
		ClassTransformerService classTransformerService = ServiceFactory.getClassTransformerService();
		CoreService coreService = ServiceFactory.getCoreService();
		if(classTransformerService != null && coreService != null && tracerService != null) {
			tracerService.registerTracerFactory(TRACER_FACTORY, new RxJava2Factory());
			
			InstrumentationContextManager contextMgr = classTransformerService.getContextManager();
			InstrumentationProxy proxy = coreService.getInstrumentation();
			
			if(contextMgr != null && proxy != null) {
				RxJava2ClassTransformer transformer = new RxJava2ClassTransformer(contextMgr);
				ClassAndMethodMatcher matcher = new RxJava2ClassAndMethodMatcher();
				transformer.addMatcher(matcher);
				return true;
			}
		}
		return false;
	}
	
	private static class SetupProcess implements Runnable {
		
		

		@Override
		public void run() {
			boolean setupComplete = false;
			
			while(!setupComplete) {
				setupComplete = setup();
				if(!setupComplete) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
					}
				}
			}
			
		}
		
	}

}
