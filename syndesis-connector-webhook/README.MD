# Syndesis Webhook Connector

This connector allows to expose a HTTP endpoint in order to receive triggers from external platforms. 

## Usage

Syndesis integrations are not exposed automatically to receive HTTP calls.

You need to expose them manually using the following script:

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