#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ $# -ne 2 ]
then
  echo "Usage: RULE_PAT OUTPUT_DIR"
  echo $@
  exit 1
fi

RULE_PAT=$1
OUTPUT_DIR=$2

# ##SOURCING DEFAULTS
source $SCRIPT_DIR/ENV-REMOTE.sh

echo MAVEN_BIN: $MAVEN_BIN
echo DEFAULT_KB_SERVER: $DEFAULT_KB_SERVER
echo DEFAULT_KB_PORT: $DEFAULT_KB_PORT
echo DEFAULT_KB: $DEFAULT_KB
echo DEFAULT_UNAME: $DEFAULT_UNAME
echo DEFAULT_PASS: $DEFAULT_PASS

echo DEFAULT_KABOB_DATA_ROOT: $DEFAULT_KABOB_DATA_ROOT

echo RULE_PAT: $RULE_PAT
echo OUTPUT_DIR: $OUTPUT_DIR

$SCRIPT_DIR/run-rules.sh $MAVEN_BIN $DEFAULT_KB_SERVER $DEFAULT_KB_PORT $DEFAULT_KB $DEFAULT_UNAME $DEFAULT_PASS $OUTPUT_DIR $RULE_PAT
