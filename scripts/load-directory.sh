#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

EXPECTED_ARGS=6

#${KABOB_DATA_ROOT:?"Need to set KABOB_DATA_ROOT non-empty"}

#if [ $# -ne $EXPECTED_ARGS ] || [ $# -ne ($EXPECTED_ARGS + 1) ]
NUM_ARGS=$#
if [ $NUM_ARGS -lt 6 ] || [ $NUM_ARGS -gt 7 ]
then
  echo "Usage: AG_BIN KB_PORT KB_NAME KABOB_ROOT RELATIVE_DIR UNIQUE_NAME (FORMAT)"
  echo $@
  exit 1
fi

AG_BIN=$1
PORT=$2
KB=$3
KABOB_DATA_ROOT=$4
LOAD_DIR=$KABOB_DATA_ROOT/$5
UNIQUE_NAME=$6
SPECIFIED_FORMAT=$7
FORMAT=${SPECIFIED_FORMAT:-"ntriples"}

# ##SOURCING DEFAULTS
# source $KABOB_DATA_ROOT/scripts/ENV.sh



LIST_FILE_RELATIVE=temp/$UNIQUE_NAME
LIST_FILE=$KABOB_DATA_ROOT/$LIST_FILE_RELATIVE

PARENT_DIR=`dirname $LIST_FILE`

mkdir -p $KABOB_DATA_ROOT/temp
mkdir -p $PARENT_DIR
`ls -1 $LOAD_DIR/* > $LIST_FILE`

echo AG_BIN: $AG_BIN
echo KB: $KB
echo PORT: $PORT
echo KABOB_DATA_ROOT: $KABOB_DATA_ROOT
echo SPECIFIED_FORMAT: $SPECIFIED_FORMAT
echo FORMAT: $FORMAT
# echo DEFAULT_KB_INDICES: $DEFAULT_KB_INDICES


echo Loading directory: $LOAD_DIR

$SCRIPT_DIR/load-list-file.sh $AG_BIN $PORT $KB $KABOB_DATA_ROOT $LIST_FILE_RELATIVE $FORMAT

#
# /home/agraph/$AG/bin/agload -v --bulk  --with-indices "ospgi posgi spogi" -p $PORT --input ntriples --encoding utf8 -d delete $KB $ROOT/temp/empty.nt
#
