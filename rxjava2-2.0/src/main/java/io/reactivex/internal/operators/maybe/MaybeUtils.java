package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;

public class MaybeUtils {

	public static <T> boolean ignore(Maybe<T> maybe) {
		
		if(maybe instanceof MaybePeek) return true;
		
		String classname = maybe.getClass().getSimpleName().toLowerCase();
		
		return classname.endsWith("on");
	}
}
