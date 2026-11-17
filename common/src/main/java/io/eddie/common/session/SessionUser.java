package io.eddie.common.session;

import java.io.Serializable;


public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String custNo;
    private String custNm;
    private String loginId;

    public String getCustNo() { return custNo; }
    public void setCustNo(String custNo) { this.custNo = custNo; }
    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }
    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
}
