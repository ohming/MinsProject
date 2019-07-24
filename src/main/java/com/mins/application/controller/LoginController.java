package com.mins.application.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mins.application.dao.Member;
import com.mins.application.service.LoginService;
import com.mins.application.service.MemberService;
import com.mins.application.utils.CookieBox;
import com.mins.application.utils.CryptEncoding;

@Controller
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req, HttpServletResponse res, Model model,
			@RequestParam(name = "cust_id", defaultValue = "") String account,
			@RequestParam(name = "cust_pass", defaultValue = "") String pwd) {

		if (account.isEmpty() && pwd.isEmpty()) {
		} else {
			if (loginService.login(res, account, pwd)) {
				return "redirect:/";
			} else {
				model.addAttribute("resultCode", "fail");
				model.addAttribute("resultMessage", "일치하지 않습니다.");
			}
		}
		return "login/login";
	}
	
	@RequestMapping("/join")
	public String join(HttpServletRequest req, HttpServletResponse res, Model model,
			@RequestParam(name = "cust_id", defaultValue = "") String cust_id,
			@RequestParam(name = "cust_pass", defaultValue = "") String cust_pass,
			@RequestParam(name = "recust_pass", defaultValue = "") String recust_pass) {
		if(recust_pass.equals("") || cust_id.equals("")) {
			return "login/join";
		}
		else if ( !recust_pass.equals(cust_pass)) {
			model.addAttribute("resultCode", "fail");
			model.addAttribute("resultMessage", "비밀번호를 확인해주세요");
			return "login/join";
		} else {
			PasswordEncoder passwordEncoder = new CryptEncoding();
			Member memberCheck = memberService.getMember(cust_id);
			if (memberCheck == null) { 
				Member member = new Member(cust_id, passwordEncoder.encode(cust_pass), Timestamp.valueOf(LocalDateTime.now()));
				memberService.save(member);
				CookieBox.login(res, cust_id);
				model.addAttribute("resultCode", "success");
				model.addAttribute("resultMessage", "회원가입이 완료되었습니다.");
				return "login/login";
			}else {
				model.addAttribute("resultCode", "fail");
				model.addAttribute("resultMessage", "이미 등록된 회원입니다.");
				return "login/join";
			}
		}
		
	}
	

	@RequestMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res) {

		return loginService.logout(res);
	}

}
