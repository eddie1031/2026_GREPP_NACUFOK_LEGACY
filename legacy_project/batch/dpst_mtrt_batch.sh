#!/bin/sh

# 만기 해지 배치
BASE_DT=`date +%Y%m%d`
CP=/app/dpst/lib/*:/app/dpst/classes

java -cp "$CP" com.bank.dpst.batch.DpstMtrtBatch $BASE_DT >> /app/dpst/logs/mtrt_$BASE_DT.log 2>&1
