package com.newrelic.instrumentation.labs.test.rxjava2;

import java.util.Random;

import com.newrelic.api.agent.Trace;

import io.reactivex.functions.Action;

public class MyCompletedAction implements Action {
	private static Random random = new Random();
	private static int MAXUNITS = 15;

	@Override
	@Trace(dispatcher=true)
	public void run() throws Exception {
		pauseRandomUnits();
		System.out.println("Object has completed");
	}
	
	private void pauseRandomUnits() {
		int n = random.nextInt(MAXUNITS);
		pause(n*100L);
	}

	private void pause(long ms) {
		if(ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}


}