package io.syndesis.extension.text;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperties;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

@Action(id = "bytesToString", name = "Bytes To String Converter", description = "Convert bytes into a string", tags = {"extension"})
@ConfigurationProperties({
  @ConfigurationProperty(name = "charset", displayName = "Character Set", description = "The character set of the input bytes", type = "string", required = true)
})
public class BytesToStringAction extends AbstractBytesToStringAction {
}
