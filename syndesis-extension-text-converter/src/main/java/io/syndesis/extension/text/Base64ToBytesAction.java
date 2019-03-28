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

@Action(id = "base64ToBytes", name = "Base64 To Bytes Converter", description = "Convert a base64 string into bytes", tags = {"extension"})
public class Base64ToBytesAction implements Step {

  @Override
  public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(Base64.decodeBase64(exchange.getIn().getBody(String.class)));
    }));
  }
}
