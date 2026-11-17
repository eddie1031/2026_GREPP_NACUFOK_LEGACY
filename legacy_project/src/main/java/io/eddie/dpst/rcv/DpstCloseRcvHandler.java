package io.eddie.dpst.rcv;

import java.util.HashMap;
import java.util.Map;

import io.eddie.common.link.RcvHandler;
import io.eddie.common.link.RcvMessage;
import io.eddie.common.link.SndMessage;
import io.eddie.common.util.StringUtil;

import io.eddie.dpst.dao.DpstCloseDao;
import io.eddie.dpst.service.DpstCloseService;

public class DpstCloseRcvHandler implements RcvHandler {

    public static final String TRX_CD_CLOSE = "DP3010";
    public static final String TRX_CD_EXPECT = "DP3011";

    private DpstCloseService dpstCloseService;
    private DpstCloseDao dpstCloseDao;

    public void setDpstCloseService(DpstCloseService dpstCloseService) { this.dpstCloseService = dpstCloseService; }
    public void setDpstCloseDao(DpstCloseDao dpstCloseDao) { this.dpstCloseDao = dpstCloseDao; }

    public SndMessage handle(RcvMessage msg) throws Exception {
        Map param = new HashMap();
        param.put("acno", msg.getField("ACNO"));
        param.put("cnclDt", msg.getField("TRX_DT"));
        Map result = dpstCloseService.procClose(param);

        SndMessage snd = new SndMessage(TRX_CD_CLOSE);
        snd.setField("INTR_AMT", StringUtil.lpad(result.get("intrAmt").toString(), 13, '0'));
        snd.setField("TAX_AMT", StringUtil.lpad(result.get("taxAmt").toString(), 13, '0'));
        snd.setField("PYM_AMT", StringUtil.lpad(result.get("pymAmt").toString(), 15, '0'));
        return snd;
    }

    public SndMessage handleExpect(RcvMessage msg) throws Exception {
        Map param = new HashMap();
        param.put("acno", msg.getField("ACNO"));
        param.put("cnclDt", msg.getField("TRX_DT"));
        Map expect = dpstCloseDao.selectExpectIntr(param);

        SndMessage snd = new SndMessage(TRX_CD_EXPECT);
        snd.setField("INTR_AMT", StringUtil.lpad(expect.get("EXPECT_INTR").toString(), 13, '0'));
        return snd;
    }

}
