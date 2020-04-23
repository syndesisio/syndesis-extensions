package io.syndesis.extension.header;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "setHeader", name = "Set Header", description = "Set an header", tags = { "header", "extension"})
public class SetHeaderAction implements Step {

    @ConfigurationProperty(
        name = "name",
        displayName = "Header name",
        description = "The header name to work on",
        type = "string" ,
        required = true)
    private String name;

    @ConfigurationProperty(
        name = "value",
        displayName = "Header Value",
        description = "The header value",
        type = "string" ,
        required = true)
    private String value;


    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    @Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(name, "name");
        ObjectHelper.notNull(value, "value");

        route.setHeader(name).constant(value);
        return Optional.empty();
    }
}
