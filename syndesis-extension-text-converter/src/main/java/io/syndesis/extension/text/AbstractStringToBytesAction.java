package io.syndesis.extension.text;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

public class AbstractStringToBytesAction implements Step {

  protected String charset;

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  @Override
  public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");
    ObjectHelper.notNull(charset, "charset");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(exchange.getIn().getBody(String.class).getBytes(charset));
    }));
  }
}
