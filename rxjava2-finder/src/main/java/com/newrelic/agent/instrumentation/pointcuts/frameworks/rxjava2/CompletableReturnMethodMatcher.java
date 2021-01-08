package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava2;

import java.util.Set;

import com.newrelic.agent.deps.org.objectweb.asm.Type;
import com.newrelic.agent.deps.org.objectweb.asm.commons.Method;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.util.asm.Utils;

public class CompletableReturnMethodMatcher implements MethodMatcher {
	
	private static final String rxClassname = "io.reactivex.Completable";
	
	public CompletableReturnMethodMatcher() {
	}
 
	@Override
	public Method[] getExactMethods() {
		return null;
	}

	@Override
	public boolean matches(int access, String name, String desc, Set<String> annotations) {
		Type type = Type.getReturnType(desc);
		String classname = type.getClassName();
		return isCompletable(classname);
	}

	private boolean isCompletable(String className) {
		if(Utils.isPrimitiveType(className) || className.endsWith("[]") || className.startsWith("com.newrelic")) {
			return false;
		}
		if(className.equals(rxClassname)) {
			return true;
		}
		if(className.startsWith("io.reactivex.internal.operators.completable") && !className.contains("$")) {
			return true;
		}

		if(className.startsWith("io.reactivex.internal.operators.flowable") && className.endsWith("Completable")) {
			return true;
		}

		if(className.startsWith("io.reactivex.internal.operators.maybe") && className.endsWith("Completable")) {
			return true;
		}

		if(className.startsWith("io.reactivex.internal.operators.observable") && className.endsWith("Completable")) {
			return true;
		}

		if(className.startsWith("io.reactivex.internal.operators.single") && className.endsWith("Completable")) {
			return true;
		}

		return false;
	}
	
}
