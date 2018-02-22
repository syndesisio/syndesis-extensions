package io.syndesis.extension.error.rng;

import java.util.Map;
import java.util.Random;

import io.syndesis.extension.api.annotations.Action;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.RuntimeCamelException;

@Action(id = "rng-error", name = "Random Error", description = "Generate a Random Error", tags = { "error", "testing", "extension"})
public class RngErrorAction {

    @Handler
    public void handle(@Body String body, @Headers Map headers, Exchange exchange) {
        Random random = new Random(System.currentTimeMillis());
        if( random.nextBoolean() ) {
            throw new RuntimeCamelException("Random error.. try your luck again next time.");
        }
    }
}
