# Validate Extension

This is a simple Syndesis Extension to add a validation rule to your route. It implements the [validate EIP](https://camel.apache.org/components/latest/eips/validate-eip.html) as defined for Apache Camel integration platform. 

If the incoming message does not match the expected rule, then an Exception is thrown, terminating the flow execution.

The extension exposes one configurable field:
- **rule** the rule as expected by [simple expression language](https://camel.apache.org/components/latest/languages/simple-language.html).



