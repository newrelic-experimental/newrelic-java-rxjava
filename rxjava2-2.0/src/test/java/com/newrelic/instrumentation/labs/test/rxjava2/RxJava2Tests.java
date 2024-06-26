package com.newrelic.instrumentation.labs.test.rxjava2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.newrelic.agent.introspec.InstrumentationTestConfig;
import com.newrelic.agent.introspec.InstrumentationTestRunner;
import com.newrelic.agent.introspec.Introspector;

import io.reactivex.Observable;

@RunWith(InstrumentationTestRunner.class)
//Tell the test harness which classes are part of the instrumentation module
@InstrumentationTestConfig(includePrefixes = { "io.reactivex" })
public class RxJava2Tests {

	@Test
	public void testObservable() {
		System.out.println("Call to testObservable");
		Introspector introspector = InstrumentationTestRunner.getIntrospector();
		Observable<String> observable = Observable.fromCallable(new MyCallable());
		observable.subscribe(new MyStringConsumer(), new MyErrorConsumer(), new MyCompletedAction());
		
		
		
	}
	
	public void doObservable() {
		
	}
}
