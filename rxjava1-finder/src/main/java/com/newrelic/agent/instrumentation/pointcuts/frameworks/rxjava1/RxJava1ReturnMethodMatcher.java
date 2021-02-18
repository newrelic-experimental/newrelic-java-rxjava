package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.newrelic.agent.instrumentation.methodmatchers.ManyMethodMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;

public class RxJava1ReturnMethodMatcher extends ManyMethodMatcher {
	
	private static CompletableReturnMethodMatcher completableMatcher = new CompletableReturnMethodMatcher();
	private static ObservableReturnMethodMatcher observableMatcher = new ObservableReturnMethodMatcher();
	private static SingleReturnMethodMatcher singleMatcher = new SingleReturnMethodMatcher();
	
	private static List<MethodMatcher> matchers;
	
	static {
		matchers = new ArrayList<MethodMatcher>();
		matchers.add(completableMatcher);
		matchers.add(observableMatcher);
		matchers.add(singleMatcher);
	}
	
	public RxJava1ReturnMethodMatcher() {
		super(matchers);
	}

	@Override
	public boolean matches(int access, String name, String desc, Set<String> annotations) {
		for(MethodMatcher matcher : matchers) {
			if(matcher.matches(access, name, desc, annotations)) {
				return true;
			}
		}
		return false;
	}

}
