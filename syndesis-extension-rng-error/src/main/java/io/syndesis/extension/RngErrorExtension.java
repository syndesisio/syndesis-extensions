package io.syndesis.extension;

import io.syndesis.extension.api.SyndesisExtensionAction;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.RuntimeCamelException;

import java.util.Map;
import java.util.Random;

@SyndesisExtensionAction(id = "rng-error", name = "Random Error", description = "Generate a Random Error")
public class RngErrorExtension {

    @Handler
    public void runScript(@Body String body, @Headers Map headers, Exchange exchange) {
        Random random = new Random(System.currentTimeMillis());
        if( random.nextBoolean() ) {
            throw new RuntimeCamelException("Random error.. try your luck again next time.");
        }
    }
}
