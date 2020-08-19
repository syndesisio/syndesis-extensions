package io.syndesis.extension.csv;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;

@Action(id = "csv-marshal", name = "csv Marshal", description = "Marshalling the exchange as csv", tags = { "marshalling", "extension" })
public class CsvMarshalAction implements Step {
	@Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
		return Optional.of(route.marshal().csv());
	}
}
