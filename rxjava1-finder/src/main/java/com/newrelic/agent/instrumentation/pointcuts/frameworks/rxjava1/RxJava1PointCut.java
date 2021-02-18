package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava1;

import com.newrelic.agent.MetricNames;
import com.newrelic.agent.Transaction;
import com.newrelic.agent.instrumentation.PointCutClassTransformer;
import com.newrelic.agent.instrumentation.PointCutConfiguration;
import com.newrelic.agent.instrumentation.TracerFactoryPointCut;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.OtherRootTracer;
import com.newrelic.agent.tracers.Tracer;
import com.newrelic.agent.tracers.metricname.ClassMethodMetricNameFormat;

public class RxJava1PointCut extends TracerFactoryPointCut {
	
	public RxJava1PointCut(PointCutClassTransformer classTransformer) {
		super(new PointCutConfiguration("rxjava1"),new RxJava1ClassMatcher(), new RxJava1ReturnMethodMatcher());
	}
	
	

	@Override
	public boolean isDispatcher() {
		return true;
	}

	@Override
	protected Tracer doGetTracer(Transaction transaction, ClassMethodSignature sig, Object rx, Object[] args) {
		return new RxJava1MethodTracer(transaction, sig, rx);
	}

	private static class RxJava1MethodTracer extends OtherRootTracer {
		
		public RxJava1MethodTracer(Transaction transaction, ClassMethodSignature sig, Object rx) {
			super(transaction,sig,rx, new ClassMethodMetricNameFormat(sig, rx, MetricNames.OTHER_TRANSACTION+"/RxJava1"));
		}
		
	}
}
