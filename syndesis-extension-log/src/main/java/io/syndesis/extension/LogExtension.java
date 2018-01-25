package io.syndesis.extension;

import io.syndesis.extension.api.SyndesisActionProperty;
import io.syndesis.extension.api.SyndesisExtensionAction;
import io.syndesis.extension.api.SyndesisStepExtension;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ProcessorDefinition;

import java.util.Map;
import java.util.Optional;

@SyndesisExtensionAction(id = "log", name = "log", description = "Log data.")
@SyndesisActionProperty(name = "message", description = "The logging message", displayName = "message", type = "textarea")
public class LogExtension implements SyndesisStepExtension {
    
    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        String message = (String) parameters.get("message");
        if( message == null ) {
            message = "Example Message: ${body}";
        }
        return Optional.of(route.log(LoggingLevel.INFO,  route.getId(), route.getId(), message));
    }
}
