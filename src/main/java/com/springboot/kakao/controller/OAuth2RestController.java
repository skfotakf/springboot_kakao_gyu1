package com.springboot.kakao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.kakao.model.vo.SignInVo;
import com.springboot.kakao.service.UserService;

@RestController
@RequestMapping("/oauth2")
public class OAuth2RestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/signupcheck")
	public String signUpCheck(@RequestBody SignInVo signInVo) {
		int result = userService.signUpEmailCheck(signInVo.getUser_email());
		
		return Integer.toString(result);
	}
}
