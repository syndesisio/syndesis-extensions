# Syndesis Sample Library Extension

This is an example of a generic library extension for Syndesis. You can import the extension into Syndesis to make it available as a runtime library in an integration’s classpath.

1. Copy the library that you need to include in your runtime classpath to `/lib` or note their `Maven GAV`.
2. Edit the `pom.xml` file as follows:
    * In the `dependencies` section, include the libraries that you need to include in your runtime classpath. Comment out or replace the `library-a` provided in the sample.
    * Change `artifactId`, `version`, `name` and `description` of the library-a example with your own scheme.
3. Install the extension by using: `mvn install`
    * This command outputs a .jar file in your `/target` folder.
4. In Syndesis, use the Extension Import feature to import the generated .jar file.
5. In Syndesis, when you save an integration, you can associate the imported library extension with the integration.
