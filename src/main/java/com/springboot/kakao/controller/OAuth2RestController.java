package com.springboot.kakao.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.kakao.model.vo.SignInVo;
import com.springboot.kakao.model.vo.SignUpVo;
import com.springboot.kakao.service.UserService;

@RestController
@RequestMapping("/oauth2")
public class OAuth2RestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/signupcheck")
	public String signUpCheck(@RequestBody SignUpVo signUpVo) {
		int result = userService.signUpEmailCheck(signUpVo.getSignUpEmail().substring(0, signUpVo.getSignUpEmail().lastIndexOf("@")));
		
		return Integer.toString(result);
	}
	
	@PostMapping("/signup")
	public String signUp(@RequestBody SignUpVo signUpVo) {
		int result = userService.signUp(signUpVo);
		return Integer.toString(result);
	}
	
	@PostMapping("/signin")
	public String signIn(@RequestBody SignUpVo signUpVo, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		session.setAttribute("login_user", userService.getUser(signUpVo.getSignUpEmail().substring(0, signUpVo.getSignUpEmail().lastIndexOf("@")))); // cookie는 구글이 관리해서 필요 x
		return "1";
	
	}
	
}
