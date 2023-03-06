package com.hh.htmlmerge.common;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hh.htmlmerge.common.dto.CommonErrorResponseDTO;
import com.hh.htmlmerge.common.exception.NoCrawlingDataException;

import lombok.extern.slf4j.Slf4j;

/** 공통 Exception 처리를 위한 ControllerAdvice */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    // 모든 크롤링 결과가 Null일 경우 발생하는 커스텀 Exception
    @ExceptionHandler(NoCrawlingDataException.class)
    public ResponseEntity<CommonErrorResponseDTO> NoCrawlingDataException(Exception e){
        log.error("[ERROR - NoCrawlingDataException] " + e.getMessage());
        return new ResponseEntity<>( new CommonErrorResponseDTO( HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage() )
            , getHeader(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponseDTO> HandleException(Exception e){
        log.error("[ERROR - HandleException] " + e.getLocalizedMessage());
        return new ResponseEntity<>( new CommonErrorResponseDTO( HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage() )
            , getHeader(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        return headers;
    }
    
}
