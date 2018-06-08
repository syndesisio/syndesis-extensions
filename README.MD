# Syndesis Extension

## Build:
```bash
mvn clean install
```
if you like to build the extensions against a particular Syndesis version please set the `syndesis.version` property accordingly in the `pom.xml`. 

### Build with automatic Syndesis version detection:
Another option is to use the `syndesisServerUrl` to pass the url of a running Syndesis instance and let maven figure the right version out for you:
```bash
mvn clean install -DsyndesisServerUrl=https://your.syndesis.server.url
```
or more conveniently, if you are logged in with `oc` command on Syndesis and use a unix like OS:
```bash
mvn clean install -DsyndesisServerUrl="https://$(oc get route syndesis  --template={{.spec.host}})"
```
if you are using a developing environment chances are that you do not have a trusted certificate for your Syndesis server, in that case you can disable SSL validation by:
```bash
mvn clean install -DsyndesisServerUrl=https://your.syndesis.server.url -DsyndesisServerUrl.disableSSLvalidation=true
```
or
```bash
mvn clean install -DsyndesisServerUrl="https://$(oc get route syndesis  --template={{.spec.host}})" -DsyndesisServerUrl.disableSSLvalidation=true
```

## Deploy:
Either manually or with this companion script that uses [jq](https://stedolan.github.io/jq/) to parse json objects:
```bash
FILE_VERSION="1.0.0"
EXTENSIONS=$(ls -d */)

for i in $EXTENSIONS
do
echo
FILE_NAME="${i::-1}" # removing trailing slash

EXTENSION_ID=$(curl "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions" \
  -H "Authorization: Bearer $(oc whoami -t)"\
  -H 'Accept: */*'\
  --insecure\
  --form file=@"$FILE_NAME/target/$FILE_NAME-$FILE_VERSION.jar;type=application/x-java-archive;filename=$FILE_NAME-$FILE_VERSION.jar" | jq -r .id)

curl -X POST "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions/$EXTENSION_ID/install" \
  --insecure\
  -H "Authorization: Bearer $(oc whoami -t)"
done
```