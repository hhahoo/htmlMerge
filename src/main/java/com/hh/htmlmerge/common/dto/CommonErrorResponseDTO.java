package com.hh.htmlmerge.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonErrorResponseDTO {
    private int errorCode;
    private String errorMessage;
}
