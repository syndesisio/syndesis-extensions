package io.syndesis.extension;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.SyndesisActionProperty;
import io.syndesis.extension.api.SyndesisExtensionAction;
import io.syndesis.extension.api.SyndesisStepExtension;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@SyndesisExtensionAction(id = "delay", name = "Delay", description = "Add a delay to your exchange")
public class DelayAction implements SyndesisStepExtension {

    // ************************
    // Extension Properties
    // ************************
    
    @SyndesisActionProperty(
        name = "delay",
        displayName = "Delay",
        description = "The delay to apply to the route",
        type = "long" ,
        required = true)
    private long delay;

    // ************************
    // Extension
    // ************************

    public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(delay, "delay");

        return Optional.of(route.delay(delay));
    }
}