package io.syndesis.extension;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.SyndesisActionProperty;
import io.syndesis.extension.api.SyndesisExtensionAction;
import io.syndesis.extension.api.SyndesisStepExtension;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@SyndesisExtensionAction(id = "throttle", name = "Throttle", description = "Use the Throttle EIP")
public class ThrottleAction implements SyndesisStepExtension {

    // ************************
    // Extension Properties
    // ************************
    
    @SyndesisActionProperty(
        name = "maximumRequestCount",
        displayName = "Maximum Request count",
        description = "Maximum messages in the default time period (1000 milliseconds)",
        type = "long" ,
        required = true)
    private long maximumRequestCount;
    
    @SyndesisActionProperty(
        name = "timePeriodMillis",
        displayName = "Time Period Milliseconds",
        description = "Time Period in milliseconds in which the throttle will work",
        type = "long" ,
        required = true)
    private long timePeriodMillis = 1000L;

    // ************************
    // Extension
    // ************************

	public long getMaximumRequestCount() {
		return maximumRequestCount;
	}

	public void setMaximumRequestCount(long maximumRequestCount) {
		this.maximumRequestCount = maximumRequestCount;
	}
    
	public long getTimePeriodMillis() {
		return timePeriodMillis;
	}

	public void setTimePeriodMillis(long timePeriodMillis) {
		this.timePeriodMillis = timePeriodMillis;
	}

	@Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(maximumRequestCount, "maximumRequestCount");
        ObjectHelper.notNull(timePeriodMillis, "timePeriodMillis");
        return Optional.of(route.throttle(maximumRequestCount).timePeriodMillis(timePeriodMillis));
    }
}