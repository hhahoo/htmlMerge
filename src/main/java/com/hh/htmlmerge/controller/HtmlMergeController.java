package com.hh.htmlmerge.controller;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hh.htmlmerge.dto.HtmlMergeResponseDTO;
import com.hh.htmlmerge.service.MergeDataParsingService;

import lombok.RequiredArgsConstructor;

/** HtmlMerge API Request 및 Response 처리를 위한 Rest Controller */
@RestController
@RequiredArgsConstructor
public class HtmlMergeController {

    private final MergeDataParsingService crawlingAndParsingService;

    @GetMapping("/get/htmlmerge")
    public ResponseEntity<HtmlMergeResponseDTO> getHtmlMergeResult() {

        return new ResponseEntity<>(
            new HtmlMergeResponseDTO( HttpStatus.OK.value(), crawlingAndParsingService.getCrawlingAndParsingData() ), 
            getHeader(), HttpStatus.OK
        );
    }

    // 공통 Header 세팅을 위한 메서드
    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
    
}
