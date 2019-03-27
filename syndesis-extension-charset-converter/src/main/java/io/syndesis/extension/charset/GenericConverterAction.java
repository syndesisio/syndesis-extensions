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

@Action(id = "genericConverter", name = "Generic Character Set Converter", description = "Convert text character set", tags = {"extension"})
public class GenericConverterAction implements Step {

  @ConfigurationProperty(
          name = "inputCharset",
          displayName = "Input Character Set",
          description = "The character set of the input text",
          type = "String",
          required = true)
  private String inputCharset;

  @ConfigurationProperty(
          name = "outputCharset",
          displayName = "Output Character Set",
          description = "The character set of the output text",
          type = "String",
          required = true)
  private String outputCharset;

  public String getInputCharset() {
    return inputCharset;
  }

  public void setInputCharset(String inputCharset) {
    this.inputCharset = inputCharset;
  }

  public String getOutputCharset() {
    return outputCharset;
  }

  public void setOutputCharset(String outputCharset) {
    this.outputCharset = outputCharset;
  }

  @Override
  public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
    ObjectHelper.notNull(route, "route");
    ObjectHelper.notNull(inputCharset, "inputCharset");
    ObjectHelper.notNull(outputCharset, "outputCharset");

    return Optional.of(route.process((Exchange exchange) -> {
      exchange.getIn().setBody(new String(exchange.getIn().getBody(String.class).getBytes(inputCharset), outputCharset));
    }));
  }
}
