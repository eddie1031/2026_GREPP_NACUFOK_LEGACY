package io.eddie.common.exception;


public class BizException extends Exception {

    private static final long serialVersionUID = 1L;

    private String msgCd;

    public BizException(String msgCd) {
        super(msgCd);
        this.msgCd = msgCd;
    }

    public String getMsgCd() { return msgCd; }
}
