#!/usr/bin/env bash
mvn clean install

FILE_VERSION="1.0.0"
EXTENSIONS=$(ls -d */)

for i in $EXTENSIONS
do

FILE_NAME="${i::-1}" # removing trailing slash
echo "Building and deploying $FILE_NAME"
EXTENSION_ID=$(curl "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions" \
  -H "Authorization: Bearer $(oc whoami -t)"\
  -H 'Accept: */*'\
  --insecure\
  --form file=@"$FILE_NAME/target/$FILE_NAME-$FILE_VERSION.jar;type=application/x-java-archive;filename=$FILE_NAME-$FILE_VERSION.jar" | jq -r .id)

curl -X POST "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions/$EXTENSION_ID/install" \
  --insecure\
  -H "Authorization: Bearer $(oc whoami -t)"
done
