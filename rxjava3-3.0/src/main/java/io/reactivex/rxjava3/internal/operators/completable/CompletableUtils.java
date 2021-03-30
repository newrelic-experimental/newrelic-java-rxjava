package io.reactivex.rxjava3.internal.operators.completable;

import io.reactivex.rxjava3.core.Completable;

public class CompletableUtils {

	public static boolean ignore(Completable completable) {
		
		if(completable instanceof CompletablePeek) return true;
		
		String classname = completable.getClass().getSimpleName().toLowerCase();
				
		return classname.endsWith("on");
	}
}
