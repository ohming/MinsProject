package com.mins.application.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mins.application.dao.Member;
import com.mins.application.dao.SearchHistory;
import com.mins.application.enums.EnumBookCategory;
import com.mins.application.enums.EnumBookTarget;
import com.mins.application.service.ApiService;
import com.mins.application.service.MemberService;
import com.mins.application.service.SearchHistoryService;
import com.mins.application.utils.CookieBox;

@RestController
@RequestMapping("ajax/")
public class AjaxController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	@Autowired
	ApiService apiService;

	@Autowired
	MemberService memberService;


	@Autowired
	SearchHistoryService searchHistoryService;

	/**
	 * 책 검색 restAPI
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/searchBooks")
	public Map<String, Object> searchBooks(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("searchWord") String searchWord,
			@RequestParam(name = "target", defaultValue = "all") String target,
			@RequestParam(name = "category", defaultValue = "") String category,
			@RequestParam(name = "api", defaultValue = "kakao") String api,
			@RequestParam(name = "page", defaultValue = "1") int page) {

		String account = CookieBox.getAccount(req);
		Member member = memberService.getMember(account);
		Map<String, Object> result = new HashMap<String, Object>();
		if("naver".equals(api))
			result = apiService.searchBooksNaver(searchWord, target, category, page);
		else 
			result = apiService.searchBooks(searchWord, target, category, page);
		searchHistoryService
				.save(new SearchHistory(searchWord, target, category, Timestamp.valueOf(LocalDateTime.now()), member));

		//
		return result;
	}
	

	/**
	 * 책 정보 restAPI
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getBookinfo/isbn/{isbn}", method = RequestMethod.GET)
	public Map<String, Object> getBookinfo(@PathVariable String isbn) {

		Map<String, Object> result = apiService.searchBooks(isbn, EnumBookTarget.ISBN.getCode(),
				EnumBookCategory.전체.getCode(), 1);

		//
		return result;
	}

}
