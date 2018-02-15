package io.syndesis.extension;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.SyndesisActionProperty;
import io.syndesis.extension.api.SyndesisExtensionAction;
import io.syndesis.extension.api.SyndesisStepExtension;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@SyndesisExtensionAction(id = "removeHeader", name = "Remove Header", description = "Remove an header")
public class RemoveHeaderAction implements SyndesisStepExtension {

    // ************************
    // Extension Properties
    // ************************
    
    @SyndesisActionProperty(
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

    // ************************
    // Extension
    // ************************

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(name, "name");
        
        return Optional.of(route.removeHeader(name));
    }
}