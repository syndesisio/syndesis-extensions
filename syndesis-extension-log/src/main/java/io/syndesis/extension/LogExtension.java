package io.syndesis.extension;

import io.syndesis.extension.api.SyndesisStepExtension;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class LogExtension implements SyndesisStepExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogExtension.class);

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        String message = (String) parameters.get("message");
        if( message == null ) {
            message = "Example Message: ${body}";
        }
        return Optional.of(route.log(message));
    }
}
