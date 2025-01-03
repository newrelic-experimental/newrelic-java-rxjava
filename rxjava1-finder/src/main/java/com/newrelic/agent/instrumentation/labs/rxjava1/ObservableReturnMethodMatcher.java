package com.newrelic.agent.instrumentation.labs.rxjava1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.newrelic.agent.deps.org.objectweb.asm.Type;
import com.newrelic.agent.deps.org.objectweb.asm.commons.Method;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.util.asm.Utils;

public class ObservableReturnMethodMatcher implements MethodMatcher {
	
	private static final String rxClassname = "rx.Observable";
	private static final String CachedObservable = "rx.internal.operators.CachedObservable";
	private static final String ScalarSync = "rx.internal.util.ScalarSynchronousObservable";
	private static final List<String> classList;
	
	static {
		classList = new ArrayList<String>();
		classList.add(rxClassname);
		classList.add(CachedObservable);
		classList.add(ScalarSync);
	}
	
	public ObservableReturnMethodMatcher() {
	}
 
	@Override
	public Method[] getExactMethods() {
		return null;
	}

	@Override
	public boolean matches(int access, String name, String desc, Set<String> annotations) {
		Type type = Type.getReturnType(desc);
		String classname = type.getClassName();
		return isObservable(classname);
	}

	private boolean isObservable(String className) {
		if(Utils.isPrimitiveType(className) || className.endsWith("[]") || className.startsWith("com.newrelic")) {
			return false;
		}
		if(classList.contains(className)) {
			return true;
		}
		
		if(className.startsWith("rx.observables") && className.endsWith("Observable")) {
			return true;
		}

		if(className.startsWith("rx.observables") && className.endsWith("Subject")) {
			return true;
		}
		
		if(className.startsWith("rx.subjects") && className.endsWith("Subject")) {
			return true;
		}

		return false;
	}
	
}
