package io.syndesis.extension.validate;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import static org.apache.camel.language.simple.SimpleLanguage.predicate;

@Action(id = "validate", name = "validate", description = "Add a simple validation step to your exchange", tags = { "validate", "extension"})
public class ValidateAction implements Step {

    @ConfigurationProperty(
        name = "rule",
        displayName = "Validation rule",
        description = "The rule used to validate the incoming message",
        type = "String" ,
        required = true)
    private String rule;

    public String getRule(){
        return rule;
    }

    public void setRule(String rule){
        this.rule = rule;
    }

	@Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(rule, "rule");

        return Optional.of(route.validate(predicate(rule)));
    }
}
