package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import com.newrelic.agent.instrumentation.ClassTransformerService;
import com.newrelic.agent.instrumentation.PointCutClassTransformer;
import com.newrelic.agent.service.AbstractService;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.NewRelic;

public class RxJava3LoaderService extends AbstractService {
	
	private ExecutorService executor = null;
	
	public RxJava3LoaderService() {
		super("RxJava3LoaderService");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	protected void doStart() throws Exception {
		ClassTransformerService classTransformerService = ServiceFactory.getClassTransformerService();
		if(classTransformerService != null) {
			PointCutClassTransformer classTransformer = classTransformerService.getClassTransformer();
			if(classTransformer != null) {
				RxJava3PointCut rxjava3Pointcut = new RxJava3PointCut(classTransformer);
				boolean b = classTransformerService.addTraceMatcher(rxjava3Pointcut, "RxJava3");
				NewRelic.getAgent().getLogger().log(Level.FINE, "Result of adding RxJava3Pointcut is {0}", b);
			} else {
				NewRelic.getAgent().getLogger().log(Level.FINE, "Could not load matcher because ClassTransformer is null");
				startExecutor();
				
			}
		} else {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Could not load matcher because ClassTransformerService is null");
			startExecutor();
		}
	}

	@Override
	protected void doStop() throws Exception {

	}
	
	private boolean addTraceMatcher(ClassTransformerService classTransformerService) {
		PointCutClassTransformer classTransformer = classTransformerService.getClassTransformer();
		RxJava3PointCut rxjava3Pointcut = new RxJava3PointCut(classTransformer);
		return classTransformerService.addTraceMatcher(rxjava3Pointcut, "RxJava3");
	}
	
	private void startExecutor() {
		executor = Executors.newSingleThreadExecutor();
		RunCheck runCheck = new RunCheck();
		executor.submit(runCheck);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Submit RunCheck to executor");		
	}

	private void shutdownExecutor() {
		executor.shutdown();
		NewRelic.getAgent().getLogger().log(Level.FINE, "ReactorLoaderService executor has shut down");
	}
	
	
	private class RunCheck implements Runnable {

		@Override
		public void run() {
			boolean done = false;
			while(!done) {
				ClassTransformerService classTransformerService = ServiceFactory.getClassTransformerService();
				if(classTransformerService != null) {
					PointCutClassTransformer classTransformer = classTransformerService.getClassTransformer();
					if(classTransformer != null) {
						done = true;
						boolean b = addTraceMatcher(classTransformerService);
						NewRelic.getAgent().getLogger().log(Level.FINE, "Result of adding ReactorPointcut is {0}", b);
					}
				} else {
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException e) {
					}
				}
			}
			shutdownExecutor();
		}

	}

}
