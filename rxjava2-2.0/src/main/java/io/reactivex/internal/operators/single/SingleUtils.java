package io.reactivex.internal.operators.single;

import io.reactivex.Single;

public class SingleUtils {

	public static <T> boolean ignore(Single<T> single) {
		String classname = single.getClass().getSimpleName().toLowerCase();
		if(classname.contains("singledoon")) return true;
		if(classname.endsWith("on")) return true;
		return false;
	}
}
