package io.syndesis.extension;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.syndesis.extension.api.SyndesisActionProperty;
import io.syndesis.extension.api.SyndesisExtensionAction;

@SyndesisExtensionAction(id = "groovy", name = "groovy", description = "Run Groovy scripts.")
public class GroovyExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyExtension.class);

    // ************************
    // Extension Properties
    // ************************


    @SyndesisActionProperty(name = "script", displayName = "script", 
        description = "Here you can write Groovy script. You can also refer to the following variables, already in scope: body, exchange, sys, env.",
        defaultValue = "println \"Hello World, this is current body: $body\"",
        type = "textarea" )

    private String script;

    // ************************
    // Accessors
    // ************************

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script =  script;
    }

    // ************************
    // Extension
    // ************************

    @Handler
    public void runScript(@Body String body, @Headers Map headers, Exchange exchange) {
        try {
            Binding binding = new Binding();
            binding.setVariable("env", System.getenv());
            binding.setVariable("sys", System.getProperties());
            binding.setVariable("body", body);
            binding.setVariable("exchange", exchange);
            
            GroovyShell shell = new GroovyShell(binding);
            String expression = this.script;
            Object result = shell.evaluate(expression);
            LOGGER.debug("result: " + result);
            LOGGER.debug("Body is: {}", body);
        } catch (Exception e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }
}
