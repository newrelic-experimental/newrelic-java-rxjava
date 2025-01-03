package com.newrelic.agent.instrumentation.labs.rxjava2.finder;

import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;

public class RxJava2ClassAndMethodMatcher implements ClassAndMethodMatcher {
	
	private ClassMatcher classMatcher = null;
	private MethodMatcher methodMatcher = null;
	
	public RxJava2ClassAndMethodMatcher() {
		classMatcher = new RxJava2ClassMatcher();
		methodMatcher = new RxJava2ReturnMethodMatcher();
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
