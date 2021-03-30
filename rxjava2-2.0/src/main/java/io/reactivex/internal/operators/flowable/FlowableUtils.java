package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;

public class FlowableUtils {

	public static <T> boolean ignore(Flowable<T> flowable) {
		
		if(flowable instanceof AbstractFlowableWithUpstream) {
			return true;
		}
		
		return false;
	}
}
