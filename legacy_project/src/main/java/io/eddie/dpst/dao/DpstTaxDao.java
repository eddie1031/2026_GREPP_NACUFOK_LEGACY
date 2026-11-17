package io.eddie.dpst.dao;

import java.util.Map;

import io.eddie.common.dao.BaseDao;

public class DpstTaxDao extends BaseDao {

    public void callTaxCalc(Map param) {
        selectOne("dpstTax.callTaxCalc", param);
    }
}
