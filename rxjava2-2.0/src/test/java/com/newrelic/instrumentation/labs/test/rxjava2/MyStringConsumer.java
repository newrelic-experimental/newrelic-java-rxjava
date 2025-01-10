package com.newrelic.instrumentation.labs.test.rxjava2;

import java.util.Random;

import com.newrelic.api.agent.Trace;

import io.reactivex.functions.Consumer;

public class MyStringConsumer implements Consumer<String> {
	
	private static Random random = new Random();
	private static int MAXUNITS = 15;

	@Override
	@Trace(dispatcher=true)
	public void accept(String t) throws Exception {
		System.out.println("MyStringConsumer received: "+t);
		pauseRandomUnits();
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
				e.printStackTrace();
			}
		}
	}
}
