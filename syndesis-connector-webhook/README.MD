# Syndesis Webhook Connector

**Warning**: this extension is deprecated. It may not work properly with Syndesis version 2.x and above.

This connector allows to expose a HTTP endpoint in order to receive triggers from external platforms.

## Usage

The webhook extension can be added to Syndesis and (after creating a default connection) used as starting point in integrations.

Syndesis integrations are not exposed automatically to receive HTTP calls.
After you create the integration on Syndesis, you need to expose it manually using the following script:

```
INTEGRATION=put-the-name-of-your-integration-here

oc expose dc i-$INTEGRATION --port 8080
oc expose service i-$INTEGRATION
```

The full Webhook URL will be obtained using the command:

```
echo http://$(oc get route i-$INTEGRATION --template={{.spec.host}})/webhook/{token}
```

Where token is the secret string you have used when creating the integration.
