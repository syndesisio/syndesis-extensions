package io.syndesis.extension.text;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperties;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

@Action(id = "stringToEbcdicBytes", name = "String To EBCDIC Bytes Converter", description = "Convert a string into EBCDIC bytes", tags = {"extension"})
@ConfigurationProperties({
  @ConfigurationProperty(name = "charset", displayName = "Character Set", description = "The type of EBCDIC character set to use", type = "string", required = true, enums = {
    @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM1047)", value = "Cp1047"),
    @ConfigurationProperty.PropertyEnum(label = "500V1 (IBM500)", value = "Cp500"),
    @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM037)", value = "Cp037")
  })
})
public class StringToEbcdicBytesAction extends AbstractStringToBytesAction {
}
