package com.mins.application.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mins.application.dao.Member;
import com.mins.application.dao.SearchHistory;
import com.mins.application.enums.EnumBookCategory;
import com.mins.application.enums.EnumBookTarget;
import com.mins.application.exceptions.IllegalLoginException;
import com.mins.application.service.MemberService;
import com.mins.application.service.SearchHistoryService;
import com.mins.application.utils.CookieBox;

@Controller
@EnableAutoConfiguration
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	MemberService memberService;

	@Autowired
	SearchHistoryService searchHistoryService;

	private Member getMemberObj(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String account = CookieBox.getAccount(req);
		Member member = memberService.getMember(account);
		if (member == null) {
			throw new IllegalLoginException("로그인 되지 않았습니다.");
		}
		return member;
	}

	@RequestMapping("/")
	public String index(HttpServletRequest req, HttpServletResponse res, Model model) {
		try {
			Member member = getMemberObj(req, res);
			model.addAttribute("EnumTarget", EnumBookTarget.values());
			model.addAttribute("EnumCategory", EnumBookCategory.values());
			return "/main";
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "redirect:/login";
		}

	}

	@RequestMapping("/detail")
	public String detail(HttpServletRequest req, HttpServletResponse res, Model model,
			@RequestParam("isbn") String isbn) {
		try {
			Member member = getMemberObj(req, res);

//			Keyword bookmark = bookmarkService.getBookmark(member, isbn);
//			model.addAttribute("bookmark", bookmark);
			return "/detail";
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "redirect:/login";
		}
	}

	@RequestMapping("/keyword")
	public String bookmarks(HttpServletRequest req, HttpServletResponse res, Model model) {
		try {

			List<Map<String, Object>> keywords = searchHistoryService.findSurveyCount();
			for (Map<String, Object> result : keywords) {
			    System.out.println(result.get("search_word"));
			    System.out.println(result.get("cnt"));
			}
			model.addAttribute("keywords", keywords);
			return "/keyword";
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "redirect:/login";
		}

	}

	@RequestMapping("/searchHistory")
	public String searchHistory(HttpServletRequest req, HttpServletResponse res, Model model,
			@PageableDefault(size = 10, page = 0, sort = "regdate", direction = Direction.DESC) Pageable pageable) {
		try {
			Member member = getMemberObj(req, res);

			Page<SearchHistory> searchHistoryPage = searchHistoryService.findByMember(member, pageable);
			model.addAttribute("searchHistoryPage", searchHistoryPage);
			return "/searchHistory";
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "redirect:/login";
		}

	}

}
