package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;

public class ObervableUtils {

	public static <T> boolean ignore(Observable<T> o) {
		if(o instanceof AbstractObservableWithUpstream) return true;
		return false;
	}

}
