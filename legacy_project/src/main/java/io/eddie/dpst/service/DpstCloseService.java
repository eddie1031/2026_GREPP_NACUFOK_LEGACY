package io.eddie.dpst.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import io.eddie.common.exception.BizException;
import io.eddie.common.link.CoreLinkSender;
import io.eddie.dpst.common.DpstConst;
import io.eddie.dpst.dao.DpstCloseDao;
import io.eddie.dpst.dao.DpstTaxDao;
import io.eddie.dpst.util.IntrCalcUtil;
import io.eddie.dpst.vo.CloseVo;

public class DpstCloseService {

    private static Logger log = Logger.getLogger(DpstCloseService.class);

    private DpstCloseDao dpstCloseDao;
    private DpstTaxDao dpstTaxDao;
    private CoreLinkSender coreLinkSender;

    public void setDpstCloseDao(DpstCloseDao dpstCloseDao) { this.dpstCloseDao = dpstCloseDao; }
    public void setDpstTaxDao(DpstTaxDao dpstTaxDao) { this.dpstTaxDao = dpstTaxDao; }
    public void setCoreLinkSender(CoreLinkSender coreLinkSender) { this.coreLinkSender = coreLinkSender; }

    public Map procClose(Map param) throws Exception {
        String acno = (String) param.get("acno");
        String cnclDt = (String) param.get("cnclDt");

        Map acntMap = dpstCloseDao.selectAcntInfo(acno);
        if (acntMap == null) throw new BizException("DP0001");
        if (!"1".equals(acntMap.get("acntStcd"))) throw new BizException("DP0002");

        long intrAmt = 0;
        if (cnclDt.compareTo((String) acntMap.get("mtrtDt")) < 0) {
            intrAmt = IntrCalcUtil.calcMdtrIntr(acntMap, cnclDt);
        } else {
            intrAmt = IntrCalcUtil.calcMtrtIntr(acntMap);
            intrAmt += IntrCalcUtil.calcPrftIntr(acntMap, (String) acntMap.get("mtrtDt"));
        }

        Map taxParam = new HashMap();
        taxParam.put("acno", acno);
        taxParam.put("intrAmt", new Long(intrAmt));
        taxParam.put("pdcd", acntMap.get("pdcd"));
        dpstTaxDao.callTaxCalc(taxParam);
        long taxAmt = ((BigDecimal) taxParam.get("taxAmt")).longValue();

        Map closeMap = new HashMap();
        closeMap.put("acno", acno);
        closeMap.put("cnclDt", cnclDt);
        closeMap.put("intrAmt", new Long(intrAmt));
        closeMap.put("taxAmt", new Long(taxAmt));
        dpstCloseDao.updateAcntClose(closeMap);
        dpstCloseDao.insertCloseHist(closeMap);

        String prcpStr = acntMap.get("prcpAmt").toString();
        while (prcpStr.length() < 15) {
            prcpStr = "0" + prcpStr;
        }
        String intrStr = String.valueOf(intrAmt);
        while (intrStr.length() < 13) {
            intrStr = "0" + intrStr;
        }
        String taxStr = String.valueOf(taxAmt);
        while (taxStr.length() < 13) {
            taxStr = "0" + taxStr;
        }
        String rtStr = "0";
        if (acntMap.get("mdtrRt") != null) {
            rtStr = String.valueOf(((BigDecimal) acntMap.get("mdtrRt")).movePointRight(4).longValue());
        }
        while (rtStr.length() < 7) {
            rtStr = "0" + rtStr;
        }

        CloseVo closeVo = new CloseVo();
        closeVo.setAcno(acno);
        closeVo.setCnclDt(cnclDt);
        closeVo.setPrcpAmt(prcpStr);
        closeVo.setIntrAmt(intrStr);
        closeVo.setTaxAmt(taxStr);
        closeVo.setMdtrRt(rtStr);
        coreLinkSender.send("CR2010", closeVo, DpstConst.CORE_LINK_KEY);

        Map logMap = dpstCloseDao.selectAcntInfo(acno);
        log.info("해지완료 acno=" + acno + ", custNm=" + logMap.get("custNm") + ", intrAmt=" + intrAmt);

        Map result = new HashMap();
        result.put("acno", acno);
        result.put("intrAmt", new Long(intrAmt));
        result.put("taxAmt", new Long(taxAmt));
        result.put("pymAmt", new Long(((BigDecimal) acntMap.get("prcpAmt")).longValue() + intrAmt - taxAmt));
        return result;
    }

}
