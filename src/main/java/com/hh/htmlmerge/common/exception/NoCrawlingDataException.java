package com.hh.htmlmerge.common.exception;

/** 모든 사이트 크롤링 결과가 없을 경우 Exception을 발생 시키기 위한 커스텀 Exception */
public class NoCrawlingDataException extends RuntimeException {

    public String getMessage() {
        return "현재 모든 사이트 크롤링의 결과가 없습니다.";
    }
    
}
