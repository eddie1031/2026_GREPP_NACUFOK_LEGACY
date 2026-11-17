package io.eddie.dpst.dao;

import java.util.List;
import java.util.Map;

import io.eddie.common.dao.BaseDao;

public class DpstCloseDao extends BaseDao {

    public Map selectAcntInfo(String acno) {
        return (Map) selectOne("dpstClose.selectAcntInfo", acno);
    }

    public Map selectExpectIntr(Map param) {
        return (Map) selectOne("dpstClose.selectExpectIntr", param);
    }

    public int updateAcntClose(Map param) {
        return update("dpstClose.updateAcntClose", param);
    }

    public int insertCloseHist(Map param) {
        return insert("dpstClose.insertCloseHist", param);
    }

    public List selectCloseHistList(Map param) {
        return selectList("dpstClose.selectCloseHistList", param);
    }

    public List selectAllAcntList() {
        return selectList("dpstClose.selectAllAcntList", null);
    }

    public int updateDormant(String baseDt) {
        return update("dpstClose.updateDormant", baseDt);
    }
}
