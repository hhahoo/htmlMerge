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
import com.hh.htmlmerge.service.crawling.CrawlingService;
import com.hh.htmlmerge.service.crawling.CrawlingTaskService;
import com.hh.htmlmerge.service.parsing.ParsingService;

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
	private ParsingService parsingService;


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
	@DisplayName("Timeout 상황이 발생하였을 경우, 2번 Retry 후 타임아웃일 경우 \"\"값을 리턴")
	void checkCrawilngJsoupRetry() throws Exception {

		CompletableFuture<String> future = crawlingTaskService.crawlingTask("http://www.google.com:81/");

		assertEquals("", future.get());

	}

}
