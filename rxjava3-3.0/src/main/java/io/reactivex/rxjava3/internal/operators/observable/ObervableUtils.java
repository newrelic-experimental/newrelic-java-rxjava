package io.reactivex.rxjava3.internal.operators.observable;

import io.reactivex.rxjava3.core.Observable;

public class ObervableUtils {

	public static <T> boolean ignore(Observable<T> o) {
		if(o instanceof AbstractObservableWithUpstream) return true;
		return false;
	}

}
