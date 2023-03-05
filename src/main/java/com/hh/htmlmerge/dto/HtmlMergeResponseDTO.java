package com.hh.htmlmerge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** htmlmerge API 응답을 리턴하기 위한 Response 객체 */
@Data
@AllArgsConstructor
public class HtmlMergeResponseDTO {
    
    private int Status;

    private String Merge;

}
