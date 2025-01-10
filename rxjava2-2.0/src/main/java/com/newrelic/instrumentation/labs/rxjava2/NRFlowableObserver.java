package com.newrelic.instrumentation.labs.rxjava2;

import java.util.logging.Level;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;

public class NRFlowableObserver<T> implements Subscriber<T> {

	private Subscriber<? super T> downstream;

	private String name = null;
	private Transaction transaction = null;

	private static boolean isTransformed = false;

	public NRFlowableObserver(Subscriber<? super T> downstream, String n, Transaction txn) {
		transaction = txn;
		this.downstream = downstream;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	@Trace
	public void onSubscribe(Subscription s) {
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber",name,"onSubscribe");
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber","UnNamed_Flowable","onSubscribe");
		}
		
		if(downstream != null) {
			downstream.onSubscribe(s);
		}
	}


	@Override
	@Trace
	public void onError(Throwable e) {
		NewRelic.noticeError(e);
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber",name,"onError");
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber","UnNamed_Flowable","onError");
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver2.onError with subscriber {0}, name {1}",downstream,name);
		if (downstream != null) {
			downstream.onError(e);
		}
	}

	@Override
	@Trace
	public void onComplete() {
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber",name,"onComplete");
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber","UnNamed_Flowable","onComplete");
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserve2.onComplete with subscriber {0}, name {1}",downstream,name);
		if (downstream != null) {
			downstream.onComplete();
		}
	}

	@Override
	@Trace(async = true)
	public void onNext(T t) {
		Token token = transaction.getToken();
		if(token != null && token.isActive()) {
			token.linkAndExpire();
		} else if(token != null) {
			token.expire();
		}
		token = null;
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber",name,"onNext");
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RxJava2","Flowable","Subscriber","UnNamed_Flowable","onNext");
		}
		if (downstream != null) {
			downstream.onNext(t);
		}
	}

}
