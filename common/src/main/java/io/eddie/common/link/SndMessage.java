package io.eddie.common.link;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 응답 전문
 */
public class SndMessage {

    private String trxCd;
    private Map fields = new LinkedHashMap();

    public SndMessage(String trxCd) { this.trxCd = trxCd; }

    public String getTrxCd() { return trxCd; }
    public String getField(String name) { return (String) fields.get(name); }
    public void setField(String name, String value) { fields.put(name, value); }
    public Map getFields() { return fields; }
}
