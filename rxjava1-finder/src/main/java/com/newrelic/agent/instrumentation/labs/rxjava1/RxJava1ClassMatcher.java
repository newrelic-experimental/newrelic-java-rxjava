package com.newrelic.agent.instrumentation.labs.rxjava1;

import java.util.Collection;
import java.util.Collections;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.agent.deps.org.objectweb.asm.ClassReader;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;

public class RxJava1ClassMatcher extends ClassMatcher {

	@Override
	public Collection<String> getClassNames() {
		return Collections.emptyList();
	}

	@Override
	public boolean isMatch(Class<?> var1) {
		if(var1.isAnnotation()) return false;
		
		Package classPackage = var1.getPackage();
		boolean b = !classPackage.getName().startsWith("rx.");
		return b;
	}

	@Override
	public boolean isMatch(ClassLoader loader, ClassReader cr) {
        if (loader == null) {
            loader = AgentBridge.getAgent().getClass().getClassLoader();
        }
 		String className = cr.getClassName();
 		
		boolean b = !className.startsWith("rx/");
		return b;
	}

}
