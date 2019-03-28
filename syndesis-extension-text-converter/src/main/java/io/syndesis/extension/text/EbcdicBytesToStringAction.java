package io.syndesis.extension.text;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperties;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

@Action(id = "ebcdicBytesToString", name = "EBCDIC Bytes To String Converter", description = "Convert EBCDIC bytes into a string", tags = {"extension"})
@ConfigurationProperties({
  @ConfigurationProperty(name = "charset", displayName = "Charcter set", description = "The type of EBCDIC charcter set to use", type = "string", required = true, enums = {
    @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM1047)", value = "Cp1047"),
    @ConfigurationProperty.PropertyEnum(label = "500V1 (IBM500)", value = "Cp500"),
    @ConfigurationProperty.PropertyEnum(label = "Latin-1 (IBM037)", value = "Cp037")
  })
})
public class EbcdicBytesToStringAction extends AbstractBytesToStringAction {
}
