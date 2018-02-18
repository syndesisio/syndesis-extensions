/*
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.extension.irc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.syndesis.integration.component.proxy.ComponentProxyComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.component.irc.IrcEndpoint;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom behavior to configure Camel Endpoints, after the CamelContext has started, due to mismatch in current Syndeis/Camel lifecycle.
 */
public class CamelIrcContextConfiguration implements CamelContextConfiguration {
    Logger LOG = LoggerFactory.getLogger(CamelIrcContextConfiguration.class);
    @Override
    public void beforeApplicationStart(CamelContext camelContext) {
        LOG.debug("Before Application Start Hook");
    }

    // this logic is weak, since there might be more instances of the same irc component, but let's do it anyhow
    // - find irc related camel components
    // - extract the configuration provided via UI, via reflection, due to lack of accessor methods
    // - set the configuration to the right object in Camel-IRC component
    // - restart Camel route
    @Override
    public void afterApplicationStart(CamelContext camelContext) {
        AtomicReference<String> channels = new AtomicReference<>("");

        camelContext.getComponentNames().stream().filter(s -> s.startsWith("irc")).forEach(c -> {
            if (camelContext.getComponent(c) instanceof ComponentProxyComponent){
                ComponentProxyComponent irc = (ComponentProxyComponent) camelContext.getComponent(c);
                Class clazz = irc.getClass();
                Field field = null;
                try {
                    field = clazz.getDeclaredField("configuredOptions");
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Model in ComponentProxyComponent class might have changed. Cannot find 'configuredOptions'", e);
                }
                field.setAccessible(true);
                Map<String, Object> refMap = null;
                try {
                    refMap = (HashMap<String, Object>) field.get(irc);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error with reflection black magic. Running a in a security manager?", e);
                }

                channels.set((String) refMap.get("channels"));
            }
        });

        IrcEndpoint endpoint = (IrcEndpoint) camelContext.getEndpoints().stream().filter(it -> it instanceof IrcEndpoint).findAny().get();
        endpoint.getConfiguration().setChannel(channels.get());

        String stringId = camelContext.getRoutes().get(0).getId();
        try {
            LOG.info("About to restart Camel route to apply Camel-irc patched configuration.");
            camelContext.stopRoute(stringId);
            camelContext.startRoute(stringId);
        } catch (Exception e) {
            LOG.error("Error restarting Camel routes", e);
        }

    }
}
