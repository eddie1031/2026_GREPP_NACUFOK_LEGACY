package io.eddie.dpst.vo;

/**
 * CR2010 해지 결과 통보 전문 VO
 */
public class CloseVo {

    private String acno;
    private String cnclDt;
    private String prcpAmt;
    private String intrAmt;
    private String taxAmt;
    private String mdtrRt;

    public String getAcno() { return acno; }
    public void setAcno(String acno) { this.acno = acno; }
    public String getCnclDt() { return cnclDt; }
    public void setCnclDt(String cnclDt) { this.cnclDt = cnclDt; }
    public String getPrcpAmt() { return prcpAmt; }
    public void setPrcpAmt(String prcpAmt) { this.prcpAmt = prcpAmt; }
    public String getIntrAmt() { return intrAmt; }
    public void setIntrAmt(String intrAmt) { this.intrAmt = intrAmt; }
    public String getTaxAmt() { return taxAmt; }
    public void setTaxAmt(String taxAmt) { this.taxAmt = taxAmt; }
    public String getMdtrRt() { return mdtrRt; }
    public void setMdtrRt(String mdtrRt) { this.mdtrRt = mdtrRt; }

}
