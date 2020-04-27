package io.syndesis.extension.telegrambot;

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

@Action(id = "telegrambot", name = "Telegram Bot", description = "Implement Telegram Bot", tags = { "telegram", "extension"})
public class TelegramBotAction implements Step {

    public static final String PRE_COMMAND =
            "if(exchange.in.headers.get(\"commandbotmatch\")==null) { " +
            "  command = body.text\n" +
            "  if(command.startsWith(\"/";
    public static final String POST_COMMAND = "\")) {\n" +
            "    exchange.in.setHeader(\"commandbotmatch\", true) \n" +
            "    arguments = command.split(\" \")\n" +
            "    exchange.in.body = botLogic(arguments)\n" +
            "  } else {\n" +
            "    if(lastcommand) {\n" +
            "      exchange.setProperty(\"CamelRouteStop\", Boolean.TRUE)\n" +
            "    }\n" +
            "  }\n" +
            "} else {\n" +
            "  if(lastcommand && exchange.in.headers.get(\"commandbotmatch\")==null) {\n" +
            "    exchange.setProperty(\"CamelRouteStop\", Boolean.TRUE)\n" +
            "  }\n" +
            "}\n";

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");

    @ConfigurationProperty(
        name = "commandname",
        displayName = "Command name",
        description = "The command name your bot respond to (without the leading /)",
        required = true)
    private String commandname;

    @ConfigurationProperty(
        name = "commandimpl",
        displayName = "Command implementation",
        description = "Create your bot command by implementing the groovy function. You can also refer to the following variables, already in scope: parameters (array of the space separated command arguments, the first element is the command itself), body, message, exchange, sys, env.",
        type = "textarea",
        required = true,
        defaultValue = "def botLogic = { parameters -> //Bot implementation code goes here, must return a String }")
    private String commandimpl;

    @ConfigurationProperty(
            name = "lastImplementedCommand",
            displayName = "Is the last command",
            description = "whether or not this is the last implemented command.",
            type = "boolean",
            required = true,
            defaultValue = "true")
    private boolean lastImplementedCommand = true;

    public boolean isLastImplementedCommand() {
        return lastImplementedCommand;
    }

    public void setLastImplementedCommand(boolean lastImplementedCommand) {
        this.lastImplementedCommand = lastImplementedCommand;
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    public String getCommandimpl() {
        return commandimpl;
    }

    public void setCommandimpl(String commandimpl) {
        this.commandimpl = commandimpl;
    }

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    public String getCommandname() {
        return commandname;
    }

    public void setCommandname(String commandname) {
        this.commandname = commandname;
    }

    @Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(engine, "engine");
        StringHelper.notEmpty(commandname, "commandname");
        StringHelper.notEmpty(commandimpl, "commandimpl");

        return Optional.of(route.process(this::process));
    }

    void process(Exchange exchange) throws Exception {
        Bindings bindings = new SimpleBindings();
        bindings.put("env", System.getenv());
        bindings.put("sys", System.getProperties());
        bindings.put("body", exchange.hasOut() ? exchange.getOut().getBody() : exchange.getIn().getBody());
        bindings.put("message", exchange.hasOut() ? exchange.getOut() : exchange.getIn());
        bindings.put("exchange", exchange);
        bindings.put("lastcommand", this.lastImplementedCommand);

        String bot = commandimpl + "\n" + PRE_COMMAND + commandname + POST_COMMAND;

        this.engine.eval(bot, bindings);
    }
}
