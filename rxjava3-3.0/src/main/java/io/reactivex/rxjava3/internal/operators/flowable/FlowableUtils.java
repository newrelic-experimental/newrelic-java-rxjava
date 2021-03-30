package io.reactivex.rxjava3.internal.operators.flowable;

import io.reactivex.rxjava3.core.Flowable;

public class FlowableUtils {

	public static <T> boolean ignore(Flowable<T> flowable) {
		
		if(flowable instanceof AbstractFlowableWithUpstream) return true;
		
		return false;
	}
}
