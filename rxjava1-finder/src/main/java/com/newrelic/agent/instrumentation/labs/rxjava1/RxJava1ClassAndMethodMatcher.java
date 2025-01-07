package com.newrelic.agent.instrumentation.labs.rxjava1;

import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;

public class RxJava1ClassAndMethodMatcher implements ClassAndMethodMatcher {
	
	private ClassMatcher classMatcher = null;
	private MethodMatcher methodMatcher = null;
	
	public RxJava1ClassAndMethodMatcher() {
		classMatcher = new RxJava1ClassMatcher();
		methodMatcher = new RxJava1ReturnMethodMatcher();
	}

	@Override
	public ClassMatcher getClassMatcher() {
		return classMatcher;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

}
