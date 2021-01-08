package com.newrelic.agent.instrumentation.pointcuts.frameworks.rxjava2;

import com.newrelic.agent.MetricNames;
import com.newrelic.agent.Transaction;
import com.newrelic.agent.instrumentation.PointCutClassTransformer;
import com.newrelic.agent.instrumentation.PointCutConfiguration;
import com.newrelic.agent.instrumentation.TracerFactoryPointCut;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.OtherRootTracer;
import com.newrelic.agent.tracers.Tracer;
import com.newrelic.agent.tracers.metricname.ClassMethodMetricNameFormat;

public class RxJava2PointCut extends TracerFactoryPointCut {
	
	public RxJava2PointCut(PointCutClassTransformer classTransformer) {
		super(new PointCutConfiguration("rxjava2"),new RxJava2ClassMatcher(), new RxJava2ReturnMethodMatcher());
	}
	
	

	@Override
	public boolean isDispatcher() {
		return true;
	}



	@Override
	protected Tracer doGetTracer(Transaction transaction, ClassMethodSignature sig, Object rx, Object[] args) {
		return new RxJava2MethodTracer(transaction, sig, rx);
	}

	private static class RxJava2MethodTracer extends OtherRootTracer {
		
		public RxJava2MethodTracer(Transaction transaction, ClassMethodSignature sig, Object rx) {
			super(transaction,sig,rx, new ClassMethodMetricNameFormat(sig, rx, MetricNames.OTHER_TRANSACTION+"/RxJava2"));
		}
		
	}
}
