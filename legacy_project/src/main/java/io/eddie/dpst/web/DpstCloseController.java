package io.eddie.dpst.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import io.eddie.common.session.SessionUser;
import io.eddie.common.util.DateUtil;
import io.eddie.dpst.dao.DpstCloseDao;
import io.eddie.dpst.service.DpstCloseService;
import io.eddie.dpst.util.IntrCalcUtil;

public class DpstCloseController extends MultiActionController {

    private DpstCloseService dpstCloseService;
    private DpstCloseDao dpstCloseDao;

    public void setDpstCloseService(DpstCloseService dpstCloseService) { this.dpstCloseService = dpstCloseService; }
    public void setDpstCloseDao(DpstCloseDao dpstCloseDao) { this.dpstCloseDao = dpstCloseDao; }

    public ModelAndView close(HttpServletRequest req, HttpServletResponse res) throws Exception {
        SessionUser user = (SessionUser) req.getSession().getAttribute("USER");
        String acno = req.getParameter("acno");

        Map acntMap = dpstCloseDao.selectAcntInfo(acno);
        if (acntMap == null || !user.getCustNo().equals(acntMap.get("custNo"))) {
            return new ModelAndView("dpst/error", "msg", "본인 계좌가 아닙니다.");
        }

        Map param = new HashMap();
        param.put("acno", acno);
        param.put("cnclDt", DateUtil.getToday());
        Map result = dpstCloseService.procClose(param);
        return new ModelAndView("dpst/closeResult", result);
    }

    public void expect(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String acno = req.getParameter("acno");
        res.setContentType("application/json; charset=UTF-8");
        try {
            Map acntMap = dpstCloseDao.selectAcntInfo(acno);
            long intrAmt = IntrCalcUtil.calcMdtrIntr(acntMap, DateUtil.getToday());
            res.getWriter().write("{\"acno\":\"" + acno + "\",\"custNm\":\"" + acntMap.get("custNm")
                    + "\",\"intrAmt\":" + intrAmt + "}");
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().write("{\"error\":\"" + e.toString() + "\"}");
        }
    }

    public ModelAndView histList(HttpServletRequest req, HttpServletResponse res) throws Exception {
        SessionUser user = (SessionUser) req.getSession().getAttribute("USER");
        Map param = new HashMap();
        param.put("custNo", user.getCustNo());
        param.put("sortCol", req.getParameter("sort"));
        List list = dpstCloseDao.selectCloseHistList(param);

        Vector histList = new Vector();
        long totIntr = 0;
        for (int i = 0; i < list.size(); i++) {
            Map row = (Map) list.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            String fromDt = sdf.format(cal.getTime());
            if (((String) row.get("CNCL_DT")).compareTo(fromDt) >= 0) {
                histList.add(row);
                totIntr = totIntr + Long.parseLong(String.valueOf(row.get("INTR_AMT")));
            }
        }

        ModelAndView mav = new ModelAndView("dpst/histList");
        mav.addObject("list", histList);
        mav.addObject("totIntr", new Long(totIntr));
        return mav;
    }

}
