package com.springboot.kakao.service;

import javax.servlet.http.Cookie;

import com.springboot.kakao.model.vo.SignInVo;
import com.springboot.kakao.model.vo.SignUpVo;
import com.springboot.kakao.model.dto.UserDto;

public interface UserService {
	public int signUpEmailCheck(String signUpEmail);
	public int signUpPhoneCheck(SignUpVo signUpVo);
	public int signUp(SignUpVo signUpVo);
	public int signIn(SignInVo signInVo);
	public UserDto getUser(String user_email);
	public Cookie setUserCookie(String user_email);
	
}
