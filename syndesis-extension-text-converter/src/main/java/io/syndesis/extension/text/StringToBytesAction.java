package io.syndesis.extension.text;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperties;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

@Action(id = "stringToBytes", name = "String To Bytes Converter", description = "Convert a string into bytes", tags = {"extension"})
@ConfigurationProperties({
  @ConfigurationProperty(name = "charset", displayName = "Character Set", description = "The character set of the output bytes", type = "string", required = true)
})
public class StringToBytesAction extends AbstractStringToBytesAction {
}
