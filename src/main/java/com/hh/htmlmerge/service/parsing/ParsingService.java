package com.hh.htmlmerge.service.parsing;

import java.util.ArrayDeque;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hh.htmlmerge.service.crawling.CrawlingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParsingService {

    private static final List<Character> AlphabetArr = List.of('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    private static final List<Character> NumberArr = List.of('0','1','2','3','4','5','6','7','8','9');

    private final CrawlingService crawlingService;

    @Cacheable( cacheNames = "MergeAndParseData", cacheManager = "caffeineCacheManager")
    public String getMergeAndParsingData() {
        StringBuffer sb = crawlingService.crawlingSites();
        return parsingAndSortingData( sb.toString().replaceAll("[^a-zA-Z0-9]", "") );
    }

    private String parsingAndSortingData(String str) {
        StringBuilder sb = new StringBuilder( );

        ArrayDeque<String> alphabetQ = new ArrayDeque<>();

        boolean isUseUpper, isUseLower;
        for(char ch : AlphabetArr){
            isUseUpper = false; isUseLower= false;
            if(str.indexOf( ch ) > -1) isUseUpper = true;
            if(str.indexOf( Character.toLowerCase(ch) ) > -1) isUseLower = true;
            
            if(isUseUpper){
                if(isUseLower) alphabetQ.add(String.valueOf(ch) + String.valueOf(Character.toLowerCase(ch)));
                else alphabetQ.add(String.valueOf(ch));
            }else {
                if(isUseLower) alphabetQ.add(String.valueOf(Character.toLowerCase(ch)));
            }
        }

        ArrayDeque<Character> numberQ = new ArrayDeque<>();
        NumberArr.stream().filter( c -> str.indexOf(c) > -1).forEach( c -> numberQ.add(c));
    
        while(!alphabetQ.isEmpty() || !numberQ.isEmpty()){
            if(!alphabetQ.isEmpty()) sb.append(alphabetQ.poll());
            if(!numberQ.isEmpty()) sb.append(numberQ.poll());
        }
        
        return sb.toString();
    }
    
}
