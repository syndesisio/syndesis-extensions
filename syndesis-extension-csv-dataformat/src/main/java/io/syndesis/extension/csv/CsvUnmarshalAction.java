package io.syndesis.extension.csv;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.ProcessorDefinition;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;

@Action(id = "csv-unmarshal", name = "csv Unmarshal", description = "Unmarshalling the exchange as csv", tags = { "unmarshalling", "extension" })
public class CsvUnmarshalAction implements Step {

	@Override
	public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
		return Optional.of(route.unmarshal(new CsvDataFormat().setUseMaps(true)));
	}
}
