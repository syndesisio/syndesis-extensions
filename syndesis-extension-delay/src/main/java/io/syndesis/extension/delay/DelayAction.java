package io.syndesis.extension.delay;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "delay", name = "Delay", description = "Add a delay to your exchange", tags = { "delay", "extension"})
public class DelayAction implements Step {
    
    @ConfigurationProperty(
        name = "delay",
        displayName = "Delay",
        description = "The delay to apply to the route",
        type = "long" ,
        required = true)
    private long delay;

    public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(delay, "delay");

        return Optional.of(route.delay(delay));
    }
}