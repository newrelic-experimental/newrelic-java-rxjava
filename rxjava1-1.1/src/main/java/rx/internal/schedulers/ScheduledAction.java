package rx.internal.schedulers;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransportType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.WeaveAllConstructors;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.rxjava1.NRRxJavaHeaders;

@Weave
public abstract class ScheduledAction {

	@NewField
	private NRRxJavaHeaders nrHeaders = null;

	@WeaveAllConstructors
	public ScheduledAction() {

		if(nrHeaders == null) {
		 nrHeaders = new NRRxJavaHeaders();
		 NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(nrHeaders);
		}
	}

	@Trace(dispatcher=true)
	public void run()  {
		boolean ignore = true;
		if(nrHeaders != null) {
			if(!nrHeaders.isEmpty()) {
				NewRelic.getAgent().getTransaction().acceptDistributedTraceHeaders(TransportType.Other, nrHeaders);
				ignore = false;
			}
		}
		if(ignore) {
			NewRelic.getAgent().getTransaction().ignore();
		}
		Weaver.callOriginal();
	}

}
