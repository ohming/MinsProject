package com.mins.application.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mins.application.dao.Member;
import com.mins.application.utils.CookieBox;
import com.mins.application.utils.CryptEncoding;

@Service
public class LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	MemberService memberService;

	@Transactional
	public boolean login(HttpServletResponse res, String account, String pwd) {
		//
		Member member = memberService.getMember(account);
		if (!account.isEmpty() && !pwd.isEmpty()) {
			PasswordEncoder passwordEncoder = new CryptEncoding();
			if (member != null) {
				if (passwordEncoder.matches(pwd, member.getCust_pass())) {
					CookieBox.login(res, account);
					return true;
				}
			}
		}
		return false;
	}

	public String logout(HttpServletResponse res) {
		//
		CookieBox.logout(res);
		return "redirect:/login";
	}

}
