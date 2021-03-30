package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava3;

import java.util.Set;

import com.newrelic.agent.deps.org.objectweb.asm.Type;
import com.newrelic.agent.deps.org.objectweb.asm.commons.Method;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.util.asm.Utils;

public class SingleReturnMethodMatcher implements MethodMatcher {
	
	private static final String rxClassname = "io.reactivex.rxjava3.core.Single";
	
	public SingleReturnMethodMatcher() {
	}
 
	@Override
	public Method[] getExactMethods() {
		return null;
	}

	@Override
	public boolean matches(int access, String name, String desc, Set<String> annotations) {
		Type type = Type.getReturnType(desc);
		String classname = type.getClassName();
		return isSingle(classname);
	}

	private boolean isSingle(String className) {
		if(Utils.isPrimitiveType(className) || className.endsWith("[]") || className.startsWith("com.newrelic")) {
			return false;
		}
		if(className.equals(rxClassname)) {
			return true;
		}
		if(className.startsWith("io.reactivex.rxjava3.internal.operators.single") && !className.contains("$")) {
			return true;
		}

		if(className.startsWith("io.reactivex.rxjava3.internal.operators.completable") && className.endsWith("Single")) {
			return true;
		}

		if(className.startsWith("io.reactivex.rxjava3.internal.operators.flowable") && className.endsWith("Single")) {
			return true;
		}

		if(className.startsWith("io.reactivex.rxjava3.internal.operators.maybe") && className.endsWith("Single")) {
			return true;
		}

		if(className.startsWith("io.reactivex.rxjava3.internal.operators.observable") && className.endsWith("Single")) {
			return true;
		}

		return false;
	}
	
}
