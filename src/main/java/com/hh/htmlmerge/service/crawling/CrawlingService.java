package com.hh.htmlmerge.service.crawling;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 멀티쓰레드로 크롤링 메소드를 실행하고, 다운받은 HTML 코드를 머지하는 서비스 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingService {

    private final CrawlingTaskService crawlingTaskService;

    // application.yml 파일에 선언 된 사이트 리스트를 Read
    @Value("${target.siteList}")
    private List<String> siteList;

    public StringBuffer crawlingSites() {
        StringBuffer sb = new StringBuffer();

        // Async 메소드인 crawlingTaskService.crawlingTask를 각 URL별로 동시 실행
        // 모든 결과를 받을 때까지 Wait한 후, 결과를 머지하여 리턴
        CompletableFuture[] futureArr = siteList.stream()
                                        .map(url -> crawlingTaskService.crawlingTask(url))
                                        .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf( futureArr ).join();

        for(CompletableFuture cf : futureArr) sb.append(cf.join());
        
        log.info("[crawlingSites] MergeData Size : " + sb.length());
        return sb;
    }

    
}
