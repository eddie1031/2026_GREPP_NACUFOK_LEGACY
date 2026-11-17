package io.eddie.dpst.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.eddie.common.util.DateUtil;
import io.eddie.dpst.dao.DpstCloseDao;
import io.eddie.dpst.service.DpstCloseService;

/**
 * 만기 도래 계좌 자동 해지 배치
 * 실행: batch/dpst_mtrt_batch.sh
 */
public class DpstMtrtBatch {

    private static Logger log = Logger.getLogger(DpstMtrtBatch.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("batch-context.xml");
        DpstCloseDao dao = (DpstCloseDao) ctx.getBean("dpstCloseDao");

        String baseDt = args.length > 0 ? args[0] : DateUtil.getToday();
        List list = dao.selectAllAcntList();
        log.info("전체 계좌 " + list.size() + "건");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Map row = (Map) list.get(i);
            log.debug("check " + i + "/" + list.size() + " acno=" + row.get("acno"));
            if (!"1".equals(row.get("acntStcd"))) continue;
            if (!baseDt.equals(row.get("mtrtDt"))) continue;

            DpstCloseService svc = (DpstCloseService) ctx.getBean("dpstCloseService");
            Map param = new HashMap();
            param.put("acno", row.get("acno"));
            param.put("cnclDt", baseDt);
            try {
                svc.procClose(param);
                cnt++;
            } catch (Exception e) {
                log.error("만기해지 실패 acno=" + row.get("acno"), e);
            }
        }
        log.info("만기해지 " + cnt + "건 완료");
    }
}
