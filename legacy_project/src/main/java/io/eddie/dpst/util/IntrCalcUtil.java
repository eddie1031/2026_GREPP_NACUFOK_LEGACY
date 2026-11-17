package io.eddie.dpst.util;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

import io.eddie.common.util.DateUtil;

public class IntrCalcUtil {

    private static Logger log = Logger.getLogger(IntrCalcUtil.class);

    /**
     * 만기이자
     */
    public static long calcMtrtIntr(Map acntMap) {
        int days = DateUtil.diffDays((String) acntMap.get("opnDt"), (String) acntMap.get("mtrtDt"));
        double intr = ((BigDecimal) acntMap.get("prcpAmt")).doubleValue()
                * ((BigDecimal) acntMap.get("bsrt")).doubleValue() / 100 * days / 365;
        return (long) Math.floor(intr);
    }

    /**
     * 우대이자
     */
    public static long calcPrftIntr(Map acntMap, String endDt) {
        if (acntMap.get("prftRt") == null) return 0;
        int days = DateUtil.diffDays((String) acntMap.get("opnDt"), endDt);
        double intr = ((BigDecimal) acntMap.get("prcpAmt")).doubleValue()
                * ((BigDecimal) acntMap.get("prftRt")).doubleValue() / 100 * days / 365;
        return (long) Math.floor(intr);
    }

    /**
     * 중도해지이자
     */
    public static long calcMdtrIntr(Map acntMap, String cnclDt) {
        double mdtrRt = 0;
        if (DateUtil.diffMonths((String) acntMap.get("opnDt"), cnclDt) < 1) {
            mdtrRt = 0.1;
        } else if (DateUtil.diffMonths((String) acntMap.get("opnDt"), cnclDt) < 3) {
            mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue() * 0.3;
        } else if (DateUtil.diffMonths((String) acntMap.get("opnDt"), cnclDt) < 6) {
            mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue() * 0.5;
        } else if (DateUtil.diffMonths((String) acntMap.get("opnDt"), cnclDt) < 12) {
            mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue() * 0.6;
        } else {
            mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue() * 0.7;
        }
        // 2015.01 이전 개설분
        if (((String) acntMap.get("opnDt")).compareTo("20150101") < 0
                && DateUtil.diffMonths((String) acntMap.get("opnDt"), cnclDt) >= 6) {
            mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue() * 0.5;
        }
        if ("D099".equals(acntMap.get("pdcd")) || "D105".equals(acntMap.get("pdcd"))) {
            mdtrRt = Math.max(mdtrRt, 1.0);
        }
        if (mdtrRt > ((BigDecimal) acntMap.get("bsrt")).doubleValue()) mdtrRt = ((BigDecimal) acntMap.get("bsrt")).doubleValue();

        double intr = ((BigDecimal) acntMap.get("prcpAmt")).doubleValue() * mdtrRt / 100
                * DateUtil.diffDays((String) acntMap.get("opnDt"), cnclDt) / 365;
        log.debug("mdtrRt=" + mdtrRt + ", days=" + DateUtil.diffDays((String) acntMap.get("opnDt"), cnclDt) + ", intr=" + intr);

        acntMap.put("mdtrRt", BigDecimal.valueOf(mdtrRt));
        return (long) Math.floor(intr);
    }
}
