package com.newrelic.agent.instrumentation.labs.rxjava3.finder;

import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;

public class RxJava3ClassAndMethodMatcher implements ClassAndMethodMatcher {
	
	private ClassMatcher classMatcher = null;
	private MethodMatcher methodMatcher = null;
	
	public RxJava3ClassAndMethodMatcher() {
		classMatcher = new RxJava3ClassMatcher();
		methodMatcher = new RxJava3ReturnMethodMatcher();
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
