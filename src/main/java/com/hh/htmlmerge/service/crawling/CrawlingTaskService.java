package com.hh.htmlmerge.service.crawling;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/** Async 메소드로 Jsoup을 통해 크롤링을 하고, 전체 HTML 코드를 리턴 */
@Slf4j
@Service
public class CrawlingTaskService {

    @Async("crawlingTaskExecutor")
    public CompletableFuture<String> crawlingTask(String url){
        log.info("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Start ");


        Document doc = null;
        // 최초 실행 시 Timeout은 3초로 설정하고, Retry 시 7초로 변경하여 2번 Try 함
        int timeoutVlaue = 3000;

        for(int i=1; i<=2; i++){

            try {

                doc = Jsoup.connect(url)
                        .timeout(timeoutVlaue).get();

                log.info("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Success, Data Size: " + doc.html().length() );
                break;

            // Connection 실패일 경우, Error 로그만 출력하고 "" 값을 리턴해서 다른 쓰레드의 결과만으로 정상 처리가 될 수 있도록 함
            } catch (SocketTimeoutException e) {
                if(i==2) {
                    log.error("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Error (" + e.getLocalizedMessage() + ")" );
                } else {
                timeoutVlaue = 7000;
                log.warn("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Timeout Occured" );
                }
            } catch (IOException e) {
                log.error("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Error (" + e.getLocalizedMessage() + ")" );
            }
    }
        
        return CompletableFuture.completedFuture( doc != null ? doc.html() : "" );
    }
    
}
