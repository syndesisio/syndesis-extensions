package io.syndesis.extension.log;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ProcessorDefinition;

@Action(id = "log", name = "Log", description = "Log a message", tags = { "log", "extension"})
public class LogAction implements Step {
    @ConfigurationProperty(
        name = "message",
        description = "The logging message",
        displayName = "message",
        type = "textarea")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        if(message == null) {
           return Optional.empty();
        }

        return Optional.of(route.log(LoggingLevel.INFO, route.getId(), route.getId(), message));
    }
}
