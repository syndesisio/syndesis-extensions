# Syndesis JDBC Driver Library Extension

Showing an example of library extension for Syndesis.

The additional jar is a JDBC driver provided as system-scope dependency (no need to deploy it on a Maven repo).

Library extensions can be used to change the behavior of some connectors (e.g. the SQL connector automatically loads all libraries tagged as "jdbc-driver"). 