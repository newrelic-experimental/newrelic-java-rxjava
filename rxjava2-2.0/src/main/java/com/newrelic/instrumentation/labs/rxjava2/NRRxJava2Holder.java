package com.newrelic.instrumentation.labs.rxjava2;

import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Transaction;

public class NRRxJava2Holder<T>  {

	private Segment segment = null;
	private String name = null;
	private String rxType = "Unknown";
	private Transaction transaction = null;
	private Token token = null;
	
	public NRRxJava2Holder(String type, String n, Transaction txn) {
		name = n;
		transaction = txn;
		if(type != null && !type.isEmpty()) {
			rxType = type;
		}
		Token t = transaction.getToken();
		if(t != null && t.isActive()) {
			token = t;
		} else if(t != null) {
			t.expire();
			t = null;
		}
	}
	
	public void startSegment() {
		segment = transaction.startSegment("RxJava2/"+rxType+"/TotalTime",name);
	}

	public void linkToken() {
		if(token != null) {
			token.link();
		}
	}
	
	public void linkAndExpireToken() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
	}
	
	public void expireToken() {
		if(token != null) {
			token.expire();
			token = null;
		}
	}
	
	public void endSegment() {
		if(segment != null) {
			segment.end();
			segment = null;
		}
	}
	
	public void ignoreSegment() {
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new String("NRRxJavaHolder:{rxType = "+rxType + ", name = "+name+"}");
	}
	
	
}
