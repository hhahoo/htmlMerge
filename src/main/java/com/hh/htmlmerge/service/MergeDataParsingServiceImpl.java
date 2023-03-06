package com.hh.htmlmerge.service;

import java.util.ArrayDeque;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hh.htmlmerge.service.crawling.CrawlingService;

import lombok.RequiredArgsConstructor;

/** 머지된 문자열을 규칙에 맞게 파싱하기위한 서비스 */
@Service
@RequiredArgsConstructor
public class MergeDataParsingServiceImpl implements MergeDataParsingService {

    private static final List<Character> AlphabetArr = List.of('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    private static final List<Character> NumberArr = List.of('0','1','2','3','4','5','6','7','8','9');

    private final CrawlingService crawlingService;

    // Caffeine Local 캐싱 라이브러를 활용하여 응답 데이터를 캐싱
    @Cacheable( cacheNames = "MergeAndParseData", cacheManager = "caffeineCacheManager")
    public String getCrawlingAndParsingData() {
        StringBuilder sb = crawlingService.crawlingSites();
       
        // 머지된 문자열에서 영문과 숫자만 남김
        return parsingAndSortingData( sb.toString().replaceAll("[^a-zA-Z0-9]", "") );
    }

    private String parsingAndSortingData(String str) {
        StringBuilder sb = new StringBuilder( );

        ArrayDeque<String> alphabetQ = new ArrayDeque<>();

        // [대문자 및 소문자 정렬 > 중복제거 > 교차출력 수행] 
        // 성능을 위해, A~Z까지 반복문을 돌며, 머지된 문자열에 각 알파벳(대문자,소문자)이 포함되어 있을 경우 Queue에 Add
        // 사용된 대문자+소문자 쌍으로 최종 정렬이 되어야 하므로 Queue에 같은 알파벳의 대소문자 쌍으로 Add (ex: Aa, B, c)
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

        // [숫자 정렬 > 중복제거 수행]
        // 0~9까지 반복문을 돌며, 머지된 문자열에 각 숫자가 포함되어 있을 경우 숫자Queue에 Add
        ArrayDeque<Character> numberQ = new ArrayDeque<>();
        NumberArr.stream().filter( c -> str.indexOf(c) > -1).forEach( c -> numberQ.add(c));
    
        // [문자 + 숫자 교차 출력 수행]
        // 예시와 같이, 대문자+소문자+숫자 형태로 쌍을 이뤄서 교차 출력이 되어야 하므로 영문큐, 숫자큐에서 교차로 빼서 결과에 Append
        // ex: 영문큐[Aa, B, C, Dd, e], 숫자큐[1, 2, 4] -> [Aa1, B2, C4, De, e]
        while(!alphabetQ.isEmpty() || !numberQ.isEmpty()){
            if(!alphabetQ.isEmpty()) sb.append(alphabetQ.poll());
            if(!numberQ.isEmpty()) sb.append(numberQ.poll());
        }
        
        return sb.toString();
    }
    
}
