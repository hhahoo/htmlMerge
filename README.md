# 직무 역량 과제

- Author : 이후현 (hhahoo@kakao.com)



### 1. Environment

- JAVA 11
- Spring Boot 2.7.9
- Gradle Project

##### 1.1. Dependencies

- spring-boot-starter-web
- spring-boot-starter-cache
- jsoup (크롤링)
- caffeine (로컬캐시)
- lombok
- spring-boot-starter-test



### 2. Feature

- Spring Boot Web을 활용한 Rest API 구현
- 사이트 크롤링은 ThreadPoolTaskExecutor 및 Async 어노테이션을 활용하여 멀티스레드를 이용한 병렬처리
- Jsoup 라이브러리를 활용한 사이트 크롤링
    - Timeout 발생 시, Retry 수행 (Timeout 시간 설정 - 최초: 3초, Retry: 7초)
    - 2회 시도 후에도 Timeout 발생 시, 해당 사이트의 크롤링 결과만 제외하고 처리
- 성능을 고려한 문자열 오름 차순 및 파싱 처리
  - 머지된 문자열 정규표현식을 통해 영문과 숫자만으로 파싱
  - A ~ Z, a ~ z, 0 ~ 9 각각 반복문을 통해 위의 문자열에 해당 글자가 사용이 되었다면 문자 및 숫자 Queue에 삽입
  - 대문자 + 소문자 + 숫자 순으로 교차 출력이 되도록 각 Queue에서 Poll하여 최종 결과를 리턴 (단, 대문자+소문자는 Aa, Bb, C, d와 같이 같은 알파벳 쌍으로 정렬이 되어야 하므로, 문자는 한 쌍으로 처리)
- Caffeine 라이브러리를 활용한 로컬 캐시 적용 (Time To Live : 1시간)




### 3. API Spec


- `(GET) /get/htmlmerge` : 크롤링 데이터 주어진 기준에 따라 파싱 후 리턴 API
- **요청/응답 예시**

> [GET] http://127.0.0.1:8080/get/htmlmerge

```json
{
    "status": 200,
    "merge": "Aa0Bb1Cc2Dd3Ee4Ff5Gg6Hh7Ii8Jj9KkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"
}
```