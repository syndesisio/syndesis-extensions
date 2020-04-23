package io.syndesis.extension.convert.body;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;

@Action(id = "ConvertBody", name = "Convert Body", description = "Convert Body to a plain String", tags = { "body", "extension" })
public class ConvertBodyAction implements Step {

	@Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
		ObjectHelper.notNull(route, "route");

		route.convertBodyTo(String.class);
		return Optional.empty();
	}
}
