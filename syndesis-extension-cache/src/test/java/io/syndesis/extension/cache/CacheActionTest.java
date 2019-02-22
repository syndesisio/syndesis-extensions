package io.syndesis.extension.cache;

import javax.xml.bind.JAXBException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelHelper;
import org.apache.camel.model.RoutesDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.syndesis.common.util.Resources;
import io.syndesis.integration.runtime.IntegrationRouteBuilder;
import io.syndesis.integration.runtime.IntegrationStepHandler;

public class CacheActionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheActionTest.class);

    @Test
    public void testSkip1() throws Exception {

        IntegrationRouteBuilder routeBuilder = new IntegrationRouteBuilder("classpath:integration-skip-1.json", Resources.loadServices(IntegrationStepHandler.class));
        final CamelContext context = new DefaultCamelContext();
        context.addRoutes(routeBuilder);
        dumpRoutes(context);
        context.start();


        HelloBean.counter = 0;
        CounterBean.counter = 0;

        final ProducerTemplate template = context.createProducerTemplate();
        String result = (String) template.requestBody("direct:in", "Tampa");
        Assert.assertEquals("Hello 0: 0: Tampa", result);

        result = (String) template.requestBody("direct:in", "Tampa");
        Assert.assertEquals("Hello 1: 0: Tampa", result);

    }

    @Test
    public void testSkip2() throws Exception {

        IntegrationRouteBuilder routeBuilder = new IntegrationRouteBuilder("classpath:integration-skip-2.json", Resources.loadServices(IntegrationStepHandler.class));
        final CamelContext context = new DefaultCamelContext();
        context.addRoutes(routeBuilder);
        dumpRoutes(context);
        context.start();


        HelloBean.counter = 0;
        CounterBean.counter = 0;

        final ProducerTemplate template = context.createProducerTemplate();
        String result = (String) template.requestBody("direct:in", "Tampa");
        Assert.assertEquals("Hello 0: 0: Tampa", result);

        result = (String) template.requestBody("direct:in", "Tampa");
        Assert.assertEquals("Hello 0: 0: Tampa", result);

    }

    private void dumpRoutes(CamelContext context) throws JAXBException {
        RoutesDefinition routes = new RoutesDefinition();
        routes.setRoutes(context.getRouteDefinitions());
        LOGGER.info("Routes: \n{}", ModelHelper.dumpModelAsXml(context, routes));
    }

}
