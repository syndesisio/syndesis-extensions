package io.syndesis.extension.script;

import java.util.Map;
import java.util.Optional;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.StringHelper;

@Action(id = "script", name = "Script", description = "Run Scripts", tags = { "scripting", "extension"})
public class ScriptAction implements Step {
    public enum Language {
        groovy,
        javascript
    }

    private ScriptEngine engine;

    @ConfigurationProperty(
        name = "language",
        displayName = "Language",
        description = "The scripting language",
        required = true)
    private Language language;

    @ConfigurationProperty(
        name = "script",
        displayName = "Script",
        description = "The script code. You can also refer to the following variables, already in scope: body, message, exchange, sys, env.",
        type = "textarea" ,
        required = true)
    private String script;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = ObjectHelper.notNull(language, "language");
        this.engine = new ScriptEngineManager().getEngineByName(language.name());

        if (this.engine == null) {
            throw new IllegalArgumentException("Unable to find a ScriptEngine for language: " + language.name());
        }
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script =  script;
    }

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(engine, "engine");
        ObjectHelper.notNull(language, "language");
        StringHelper.notEmpty(script, "script");

        return Optional.of(route.process(this::process));
    }

    void process(Exchange exchange) throws Exception {
        Bindings bindings = new SimpleBindings();
        bindings.put("env", System.getenv());
        bindings.put("sys", System.getProperties());
        bindings.put("body", exchange.hasOut() ? exchange.getOut().getBody() : exchange.getIn().getBody());
        bindings.put("message", exchange.hasOut() ? exchange.getOut() : exchange.getIn());
        bindings.put("exchange", exchange);

        this.engine.eval(this.script, bindings);
    }
}