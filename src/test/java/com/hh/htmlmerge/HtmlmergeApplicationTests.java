package com.hh.htmlmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.hh.htmlmerge.controller.HtmlMergeController;
import com.hh.htmlmerge.service.MergeDataParsingServiceImpl;
import com.hh.htmlmerge.service.crawling.CrawlingService;
import com.hh.htmlmerge.service.crawling.CrawlingTaskService;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HtmlmergeApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CrawlingTaskService crawlingTaskService;

	@Autowired
	private MergeDataParsingServiceImpl parsingService;


	@Test
	@DisplayName("get/htmlmerge 전송 통합 테스트")
	void requestHtmlMergeAPI() throws Exception {

		mvc.perform(get("/get/htmlmerge"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is(200)))
			.andDo(print());

	}

	@Test
	@DisplayName("파싱 및 교차출력 메소드가 예제의 인풋 및 아웃풋과 같은지 확인")
	void checkParsingAndSortingData() throws IOException {

		String result = ReflectionTestUtils.invokeMethod(parsingService, "parsingAndSortingData", "html124divABCDefgtaBleImg1");

		assertEquals("Aa1B2C4DdefghIilmtv", result);
	}

	@Test
	@DisplayName("파싱 및 교차출력 메소드가 여러 예제에서 정상동작 하는지 확인")
	void checkParsingAndSortingDataOther() throws IOException {

		// 숫자가 남아있는 경우
		String result = ReflectionTestUtils.invokeMethod(parsingService, "parsingAndSortingData", "ABC123a456c789de0");
		assertEquals("Aa0B1Cc2d3e456789", result);
		// 문자만 있는 경우
		result = ReflectionTestUtils.invokeMethod(parsingService, "parsingAndSortingData", "ffBBAADDCChhjjggee");
		assertEquals("ABCDefghj", result);
		// 숫자만 있는 경우
		result = ReflectionTestUtils.invokeMethod(parsingService, "parsingAndSortingData", "987654321");
		assertEquals("123456789", result);
	}

	@Test
	@DisplayName("Timeout 상황이 발생하였을 경우, 2번 Retry 후 타임아웃일 경우 \"\"값을 리턴")
	void checkCrawilngJsoupRetry() throws Exception {

		CompletableFuture<String> future = crawlingTaskService.crawlingTask("http://www.google.com:81/");

		assertEquals("", future.get());

	}

}
