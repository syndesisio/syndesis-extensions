package io.syndesis.extension.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.AsyncCallback;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.NamedNode;
import org.apache.camel.Processor;
import org.apache.camel.model.PipelineDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.support.processor.DelegateAsyncProcessor;
import org.apache.camel.util.ObjectHelper;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

//@Action(id = "cache",
//        name = "Cache",
//        description = "Cache the next step in your exchange",
//        tags = {"step", "extension"},
//        inputDataShape = @DataShape(kind="none"),
//        outputDataShape = @DataShape(kind="none"))
public class CacheAction implements Step {

    @ConfigurationProperty(
            name = "steps",
            displayName = "Steps",
            description = "The number of subsequent steps to cache.",
            type = "int",
            defaultValue = "1",
            required = true)
    private int steps = 1;

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(steps, "steps");

        final AtomicReference<Message> cachedValue = new AtomicReference<>();

        final String cahceStepId = route.getId();

        route.addInterceptStrategy(new InterceptStrategy() {

            boolean countingDown = false;
            int remaining = steps;

            @Override
            public Processor wrapProcessorInInterceptors(CamelContext context, NamedNode definition, Processor target, Processor nextTarget) throws Exception {
                System.out.println("intercept pipeline " + definition.getId() + ", " + cahceStepId);

                if (definition instanceof PipelineDefinition) {
                    if (Objects.equals(cahceStepId, definition.getId())) {
                        countingDown = true;
                        return target;
                    }
                    if (countingDown) {
                        remaining --;
                        if (remaining == 0) {
                            countingDown = false;

                            // lets store the result in the cache after this step..
                            target = new DelegateAsyncProcessor(target) {
                                @Override
                                public boolean process(final Exchange exchange, final AsyncCallback callback) {
                                    Message cached = cachedValue.get();
                                    if( cached != null ) {
                                        exchange.getOut().copyFrom(cached);
                                        callback.done(true);
                                        return true;
                                    } else {
                                        return super.process(exchange, doneSync -> {
                                            cachedValue.set(exchange.getOut());
                                            callback.done(doneSync);
                                        });
                                    }
                                }
                            };
                        }

                        if (remaining > 0) {
                            // Lets skip this steps when we have a cached value.
                            target = new DelegateAsyncProcessor(target) {
                                @Override
                                public boolean process(final Exchange exchange, final AsyncCallback callback) {
                                    Message cached = cachedValue.get();
                                    if( cached!=null ) {
                                        callback.done(true); // skip this step we have cached value..
                                        return true;
                                    } else {
                                        return super.process(exchange, callback);
                                    }
                                }
                            };
                        }
                    }

                }
                return target;
            }
        });

        return Optional.empty();
    }
}
