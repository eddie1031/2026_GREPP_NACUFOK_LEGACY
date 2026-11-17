package io.eddie.common.link;

/**
 * 단말 전문 처리기
 * 거래코드와 핸들러의 매핑은 bank-common 내부 설정
 */
public interface RcvHandler {

    SndMessage handle(RcvMessage msg) throws Exception;
}
