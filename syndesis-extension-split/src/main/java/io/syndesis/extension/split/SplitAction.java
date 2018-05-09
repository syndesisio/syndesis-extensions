package io.syndesis.extension.split;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.Expression;
import org.apache.camel.builder.Builder;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.apache.camel.spi.Language;

@Action(id = "split", name = "Split", description = "Split your exchange", tags = { "split", "extension"})
public class SplitAction implements Step {
    
    @ConfigurationProperty(
        name = "language",
        displayName = "Language",
        description = "The language used for the expression")
    private String language;

    @ConfigurationProperty(
        name = "expression",
        displayName = "Expression",
        description = "The expression used to split the exchange")
    private String expression;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        String languageName = language;
        String expressionDefinition = expression;

        if (ObjectHelper.isEmpty(languageName) && ObjectHelper.isEmpty(expressionDefinition)) {
            route = route.split(Builder.body());
        } else if (ObjectHelper.isNotEmpty(expressionDefinition)) {

            if (ObjectHelper.isEmpty(languageName)) {
                languageName = "simple";
            }

            // A small hack until https://issues.apache.org/jira/browse/CAMEL-12079
            // gets fixed so we can support the 'bean::method' annotation as done by
            // Function step definition
            if ("bean".equals(languageName) && expressionDefinition.contains("::")) {
                expressionDefinition = expressionDefinition.replace("::", "?method=");
            }

            final Language splitLanguage = context.resolveLanguage(languageName);
            final Expression splitExpression = splitLanguage.createExpression(expressionDefinition);

            route = route.split(splitExpression).aggregationStrategy(new UseOriginalAggregationStrategy(null, false));
        }

        return Optional.of(route);
    }
}