# Syndesis IRC Connector

**Warning**: this extension is deprecated. It may not work properly with Syndesis version 2.x and above.

Showing an example of advanced code integration, required by a mismatch with `camel-irc` component implementation and Syndesis Lifecycle.

The code uses a custom Spring Boot class, to alter Camel Configuration and restart a route after the Camel Context has started.

This approach is required, in this case, only to set `channels` parameter.

