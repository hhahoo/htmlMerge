package com.hh.htmlmerge.service.crawling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingService {

    private final CrawlingTaskService crawlingTaskService;

    @Value("${target.siteList}")
    private List<String> siteList;

    public StringBuffer crawlingSites() {
        StringBuffer sb = new StringBuffer();

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        siteList.forEach(url -> futureList.add( crawlingTaskService.crawlingTask(url) ));

        CompletableFuture.allOf(futureList.toArray( new CompletableFuture[futureList.size()])).join();

        futureList.forEach(result -> sb.append(result.join()));
        
        log.info("[MergeData Size] " + sb.length() +" " + futureList.size() );
        return sb;
    }

    
}
