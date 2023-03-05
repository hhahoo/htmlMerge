package com.hh.htmlmerge.service.crawling;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CrawlingTaskService {

    @Async("crawlingTaskExecutor")
    public CompletableFuture<String> crawlingTask(String url){

        Document doc = null;
        int timeoutVlaue = 5000;

        for(int i=1; i<=2; i++){

            try {

                doc = Jsoup.connect(url)
                        .timeout(timeoutVlaue).get();

                log.info("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Success, Data Size: " + doc.html().length() );
                break;

            } catch (SocketTimeoutException e) {
                if(i==2) {
                    log.error("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Error (" + e.getLocalizedMessage() + ")" );
                } else {
                timeoutVlaue = 10000;
                log.warn("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Timeout Occured" );
                }
            } catch (IOException e) {
                log.error("[crawlingTask][" + url + "][" + Thread.currentThread().getName() + "] Connection Error (" + e.getLocalizedMessage() + ")" );
            }
    }
        return CompletableFuture.completedFuture( doc != null ? doc.html() : "" );
    }
    
}
