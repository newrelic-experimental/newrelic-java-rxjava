package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;

public class CompletableUtils {

	public static boolean ignore(Completable completable) {
		
		if(completable instanceof CompletablePeek) return true;
		
		String classname = completable.getClass().getSimpleName().toLowerCase();
		
		return classname.endsWith("on");
	}
}
