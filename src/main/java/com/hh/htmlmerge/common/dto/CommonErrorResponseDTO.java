package com.hh.htmlmerge.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 에러 응답을 리턴하기위한 Response 객체 */
@Data
@AllArgsConstructor
public class CommonErrorResponseDTO {
    private int errorCode;
    private String errorMessage;
}
