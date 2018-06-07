package io.syndesis.extension.throttle;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "throttle", name = "Throttle", description = "Use the Throttle EIP", tags = { "throttle", "extension", "eip"})
public class ThrottleAction implements Step {
    
    @ConfigurationProperty(
        name = "maximumRequestCount",
        displayName = "Maximum Request count",
        description = "Maximum messages in the default time period (1000 milliseconds)",
        type = "long" ,
        required = true)
    private long maximumRequestCount;
    
    @ConfigurationProperty(
        name = "timePeriodMillis",
        displayName = "Time Period Milliseconds",
        description = "Time Period in milliseconds in which the throttle will work",
        type = "long" ,
        defaultValue = "1000",
        required = false)
    private long timePeriodMillis = 1000;

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

        return Optional.of(route.throttle(maximumRequestCount).timePeriodMillis(timePeriodMillis));
    }
}