#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

EXPECTED_ARGS=5

#if [ $# -ne $EXPECTED_ARGS ]
#if [ $# -ne $EXPECTED_ARGS ] || [ $# -ne ($EXPECTED_ARGS + 1) ]
if [ $# -lt 5 ] || [ $# -gt 6 ]
then
  echo "Usage: AG_BIN KB_PORT KB_NAME KABOB_ROOT LIST_FILE (FORMAT)"
  echo $@
  exit 1
fi

AG_BIN=$1
PORT=$2
KB=$3
KABOB_DATA_ROOT=$4
LIST_FILE=$KABOB_DATA_ROOT/$5

SPECIFIED_FORMAT=$6
FORMAT=${SPECIFIED_FORMAT:-"ntriples"}

##SOURCING DEFAULTS
source $SCRIPT_DIR/ENV.sh


echo AG_BIN: $AG_BIN
echo KB: $KB
echo PORT: $PORT
echo KABOB_DATA_ROOT: $KABOB_DATA_ROOT
echo SPECIFIED_FORMAT: $SPECIFIED_FORMAT
echo FORMAT: $FORMAT
echo DEFAULT_KB_INDICES: $DEFAULT_KB_INDICES

echo Loading list file: $LIST_FILE
echo List file count:
wc -l $LIST_FILE
cat $LIST_FILE

$AG_BIN/agload --verbose --bulk --graph :source --with-indices "$DEFAULT_KB_INDICES" -p $PORT --input $FORMAT $KB @$LIST_FILE

#--debug
# --with-indices "ospgi posgi spogi" -p $PORT --input ntriples --encoding utf8 
