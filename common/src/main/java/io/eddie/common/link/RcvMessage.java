package io.eddie.common.link;

import java.util.HashMap;
import java.util.Map;

/**
 * 수신 전문
 */
public class RcvMessage {

    private String trxCd;
    private Map fields = new HashMap();

    public RcvMessage(String trxCd) { this.trxCd = trxCd; }

    public String getTrxCd() { return trxCd; }
    public String getField(String name) { return (String) fields.get(name); }
    public void setField(String name, String value) { fields.put(name, value); }
}
