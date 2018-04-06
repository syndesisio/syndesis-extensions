#!/usr/bin/env bash
mvn clean install

FILE_VERSION="1.0.0"


COOKIE1="$HOME/.syndesis_api_cookie"

# helper python command to unescape html entities
xml_unescape() {

local py_script="
import sys, HTMLParser
parser = HTMLParser.HTMLParser()
print parser.unescape(sys.argv[1])
"
python -c "$py_script" $1
}

extract_and_escape() {
    local variable=$(grep  -E name=\"$1\" $2 | grep -Po '(?<=value=")[^"]+' )
    variable="$(xml_unescape $variable)"
    echo "$variable"
}

create_api_cookie() {

    
    # second cookie needed because curl flushes old values upon redirect. Might be a bug or some obscure flag missing
    local cookie2="$(mktemp /tmp/syndesis_cookie.XXX.txt)"
    # generate a new user so that it will be asked to approve permissions delegation
    local username="dev$RANDOM"

    # clean cookie
    rm -rf "$COOKIE1"
    touch "$COOKIE1"

    local syndesis_url="$(oc get route syndesis  --template={{.spec.host}})"
    local openshift_ip_and_port="$(oc status | grep "In project" | grep  -Po --color=never '(?<=server https://).*' )"
    # first invocation is needed just to get the redierect url and to define a csrf token
    local redirect_url="$(curl --write-out "%{url_effective}\n" --location --silent --show-error --output /dev/null --insecure --cookie-jar  "$COOKIE1" -v "https://$syndesis_url" 2>/dev/null )"

    # isolate required params
    # split on question mark, so we are sure the first part is always the correct url
    local login_url="$(echo $redirect_url | sed -e 's/?/\n/g' | head -n1)"
    # split on question marks or ampersend, than filter on the specific param we are interested into
    local then_url="$(echo $redirect_url | sed -e 's/[?&]/\n/g' | grep "then=" | sed -e 's/then=//' )"
    local csrf=$(cat "$COOKIE1"  | grep -Po '(?<=\scsrf).*' | awk '{$1=$1;print}')

    # login on OpenShift and save output to delegate_permissions_form location
    local delegate_permissions_form="$(mktemp /tmp/syndesis_permissions.XXX.html)"
    curl --referer "$redirect_url" --insecure --location  -H "Cookie: csrf=$csrf" --data-urlencode username="$username"  --data-urlencode password="developer" --data-ascii then="$then_url" --data-urlencode csrf="$csrf" --cookie-jar "$cookie2" -i -v -o $delegate_permissions_form "$login_url"  2>/dev/null

    # move the freshly created "ssn" cookie entry back to the main cookie
    tail -1 "$cookie2" >> "$COOKIE1"

    local then_url=$(extract_and_escape "then" $delegate_permissions_form )
    local csrf=$(extract_and_escape "csrf" $delegate_permissions_form )
    local client_id=$(extract_and_escape "client_id" $delegate_permissions_form )
    local user_name=$(extract_and_escape "user_name" $delegate_permissions_form )
    local redirect_uri=$(extract_and_escape "redirect_uri" $delegate_permissions_form )

    local scopes=$(grep -Po '(?<=name="scope").*' $delegate_permissions_form | grep -Po '(?<=value=")[^"]+')
    local joined_scopes=""
    
    scopes=(${scopes// / })
    for i in "${!scopes[@]}"
    do
        joined_scopes="$joined_scopes --data-urlencode scope=${scopes[i]}"
    done

    # approve permission delegation, weaving all the params in the GET parameters
    
    curl --insecure --location --cookie-jar "$COOKIE1" --cookie "$COOKIE1" -i -v --data-urlencode "then=$then_url" --data-urlencode "csrf=$csrf" --data-urlencode "client_id=$client_id" --data-urlencode "user_name=$user_name" --data-urlencode "redirect_uri=$redirect_uri" $joined_scopes --data-urlencode "approve=Allow+selected+permissions"  -H 'dnt: 1'  "https://$openshift_ip_and_port/oauth/authorize/approve" -o /dev/null 2>/dev/null
}

create_api_cookie



EXTENSIONS=$(ls -d */)

for i in $EXTENSIONS
do

FILE_NAME="${i::-1}" # removing trailing slash
echo "Building and deploying $FILE_NAME"
EXTENSION_ID=$(curl "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions" \
  --cookie "$COOKIE1"\
  -H 'Accept: */*'\
  --insecure\
  --form file=@"$FILE_NAME/target/$FILE_NAME-$FILE_VERSION.jar;type=application/x-java-archive;filename=$FILE_NAME-$FILE_VERSION.jar" | jq -r .id)

curl -X POST "https://$(oc get route syndesis  --template={{.spec.host}})/api/v1/extensions/$EXTENSION_ID/install" \
  --insecure\
  --cookie "$COOKIE1"
done
