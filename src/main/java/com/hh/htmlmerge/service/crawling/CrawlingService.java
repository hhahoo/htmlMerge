package com.hh.htmlmerge.service.crawling;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hh.htmlmerge.common.exception.NoCrawlingDataException;

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

    public StringBuilder crawlingSites() {
        StringBuilder sb = new StringBuilder();

        // Async 메소드인 crawlingTaskService.crawlingTask를 각 URL별로 동시 실행
        // 모든 결과를 받을 때까지 Wait한 후, 결과를 머지하여 리턴
        List<CompletableFuture<String>> futureList = siteList.stream()
                                                             .map(url ->  crawlingTaskService.crawlingTask(url))
                                                             .collect(Collectors.toList());

        CompletableFuture.allOf(futureList.toArray( new CompletableFuture[siteList.size()]))
                         .thenRun(() ->
                            futureList.forEach(result -> sb.append(result.join()))
                         ).join();
        
        // 모든 사이트의 크롤링 결과가 Null일 경우 커스텀Exception 발생
        if( sb == null || sb.length() == 0 ) throw new NoCrawlingDataException();

        log.info("[crawlingSites] MergeData Size : " + sb.length());
        return sb;
    }

    
}
