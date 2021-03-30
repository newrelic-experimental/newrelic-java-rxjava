package io.reactivex.rxjava3.internal.operators.maybe;

import io.reactivex.rxjava3.core.Maybe;

public class MaybeUtils {

	public static <T> boolean ignore(Maybe<T> maybe) {
		
		if(maybe instanceof MaybePeek) return true;
		
		String classname = maybe.getClass().getSimpleName().toLowerCase();
		
		return classname.endsWith("on");
	}
}
