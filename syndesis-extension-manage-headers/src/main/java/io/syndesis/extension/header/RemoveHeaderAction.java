package io.syndesis.extension.header;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "removeHeader", name = "Remove Header", description = "Remove an header", tags = { "header", "extension"})
public class RemoveHeaderAction implements Step {
    
    @ConfigurationProperty(
        name = "name",
        displayName = "Header name",
        description = "The header name to remove",
        type = "string" ,
        required = true)
    private String name;
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(name, "name");
        
        return Optional.of(route.removeHeader(name));
    }
}