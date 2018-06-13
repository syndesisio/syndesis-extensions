package io.syndesis.extension.loop;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "loop", name = "Loop", description = "Add a loop to your exchange", tags = { "loop", "extension"})
public class LoopAction implements Step {
    
    @ConfigurationProperty(
        name = "cycles",
        displayName = "Cycles",
        description = "The loop cycles to apply to the route",
        type = "int" ,
        required = true)
    private int cycles;
    
	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

	@Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(cycles, "cycles");

        return Optional.of(route.loop(cycles));
    }
}