package com.hh.htmlmerge.controller;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hh.htmlmerge.dto.HtmlMergeResponseDTO;
import com.hh.htmlmerge.service.parsing.ParsingService;

import lombok.RequiredArgsConstructor;

/** HtmlMerge API Request 및 Response 처리를 위한 Rest Controller */
@RestController
@RequiredArgsConstructor
public class HtmlMergeController {

    private final ParsingService parsingService;

    @GetMapping("/get/htmlmerge")
    public ResponseEntity<HtmlMergeResponseDTO> getHtmlMergeResult() {

        return new ResponseEntity<>(
            new HtmlMergeResponseDTO( HttpStatus.OK.value(), parsingService.getMergeAndParsingData() ), 
            getHeader(), HttpStatus.OK
        );
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
    
}
