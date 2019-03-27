package io.syndesis.extension.charset;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "asciiToEbcdic", name = "ASCII To EBCDIC Converter", description = "Convert text from the ASCII to the EBCDIC character set", tags = {"extension"})
public class AsciiToEbcdicAction implements Step {

  @ConfigurationProperty(
          name = "charset",
          displayName = "Character Set",
          description = "The type of EBCDIC character set to use",
          type = "String",
          required = true,
          enums = {
            @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM1047)", value = "Cp1047"),
            @ConfigurationProperty.PropertyEnum(label = "500V1 (IBM500)", value = "Cp500"),
            @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM037)", value = "Cp037")})
  private String charset;

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  @Override
  public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");
    ObjectHelper.notNull(charset, "charset");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(new String(exchange.getIn().getBody(String.class).getBytes("US-ASCII"), charset));
    }));
  }
}
