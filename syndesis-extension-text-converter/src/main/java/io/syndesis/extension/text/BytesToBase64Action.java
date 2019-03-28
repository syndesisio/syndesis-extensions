package io.syndesis.extension.text;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.codec.binary.Base64;

@Action(id = "bytesToBase64", name = "Bytes To Base64 Converter", description = "Convert bytes into a base64 string", tags = {"extension"})
public class BytesToBase64Action implements Step {

  @Override
  public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(Base64.encodeBase64String(exchange.getIn().getBody(byte[].class)));
    }));
  }
}
