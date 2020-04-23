package io.syndesis.extension.text;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.codec.binary.Hex;

@Action(id = "hexToBytes", name = "Hex To Bytes Converter", description = "Convert a hex string into bytes", tags = {"extension"})
public class HexToBytesAction implements Step {

  @Override
  public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(Hex.decodeHex(exchange.getIn().getBody(String.class).toCharArray()));
    }));
  }
}
