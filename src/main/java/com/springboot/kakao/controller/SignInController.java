package com.springboot.kakao.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.kakao.model.vo.SignInVo;
import com.springboot.kakao.service.UserService;
import com.springboot.kakao.model.dto.UserDto;

@Controller
public class SignInController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "sign-in", method = RequestMethod.GET)
	public String signInIndex(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("user_email")) {
					UserDto userDto = userService.getUser(c.getValue());
					session.setAttribute("login_user", userDto);
				}
			}
		}
		if(session.getAttribute("login_user") != null) {
			return "redirect:index";
		}
		return "signIn/sign_in";
	}

	
	@ResponseBody
	@RequestMapping(value = "sign-in", method = RequestMethod.POST)
	public Object signIn(@RequestBody SignInVo signInVo, HttpServletRequest request, HttpServletResponse response) {
		
		signInVo.setSignInFlag(userService.signIn(signInVo));
		System.out.println(signInVo);
		if(signInVo.getSignInFlag() == 2) {
			
			HttpSession session = request.getSession();
			session.setAttribute("login_user", userService.getUser(signInVo.getUser_email()));
			if(signInVo.getSignIncb().equals("on")) {
				Cookie cookie = userService.setUserCookie(signInVo.getUser_email());
				response.addCookie(cookie);
			}
			
		}
		
		return signInVo;
	}
}
