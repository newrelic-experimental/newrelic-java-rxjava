package rx.internal.schedulers;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.WeaveAllConstructors;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class ScheduledAction {
	
	@NewField
	private Token token = null;
	
	@WeaveAllConstructors
	public ScheduledAction() {
		if(token == null) {
			Token t = NewRelic.getAgent().getTransaction().getToken();
			if(t != null && t.isActive()) {
				token = t;
			} else if(t != null) {
				t.expire();
				t = null;
			}
		}
	}
	
	@Trace(async=true)
	public void run() { 
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		Weaver.callOriginal();
	}

}
