package com.newrelic.instrumentation.rxjava2;

import java.util.logging.Level;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;


public class NRFlowableObserver<T> implements Subscriber<T>, Subscription, NRObserver {

	private Subscriber<? super T> downstream;
	
	Subscription upstream;
	public Token token = null;
	private Segment segment = null;
	
	private static boolean isTransformed = false;
	
	public NRFlowableObserver(Subscriber<? super T> s) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Constructed NRFlowableObserver.<init> with subscriber {0}",s);
		downstream = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}
	
	public void setSegment(Segment s) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver.setSegment with subscriber {0}, using segment {1}",downstream,s);
		segment = s;
	}
	
	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onNext(T t) {
		if(token != null) {
			token.link();
		}
		downstream.onNext(t);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onError(Throwable t) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver.onError with subscriber {0}",downstream);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.end();
			segment = null;
		}
		downstream.onError(t);
	}

	@Override
	@Trace(async=true,excludeFromTransactionTrace=true)
	public void onComplete() {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to NRFlowableObserver.onComplete with subscriber {0}",downstream);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "In NRFlowableObserver.onComplete ending segment {0}",segment);
			segment.end();
			segment = null;
		}
		downstream.onComplete();
	}

	@Override
	public void request(long n) {
		upstream.request(n);
	}

	@Override
	public void cancel() {
		upstream.cancel();
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
		if(token != null) {
			token.expire();
			token = null;
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		if(upstream != null) {
			s.cancel();
		} else {
			upstream = s;
			downstream.onSubscribe(this);
		}
	}

}
