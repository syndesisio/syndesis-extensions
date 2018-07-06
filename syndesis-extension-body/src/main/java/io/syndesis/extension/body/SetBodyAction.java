package io.syndesis.extension.body;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

@Action(id = "setBody", name = "Set Body", description = "Set your body", tags = { "body", "extension" })
public class SetBodyAction implements Step {

	@ConfigurationProperty(name = "body", displayName = "Body", description = "The body to set", type = "string", required = true)
	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
        public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> definition, Map<String, Object> parameters) {
		ObjectHelper.notNull(definition, "route");
		ObjectHelper.notNull(body, "body");

		definition.setBody().constant(body);
		return Optional.empty();
	}
}
