package com.springboot.kakao.service;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.kakao.model.vo.SignInVo;
import com.springboot.kakao.model.vo.SignUpVo;
import com.springboot.kakao.mapper.UserDao;
import com.springboot.kakao.model.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public int signUpEmailCheck(String signUpEmail) {
		return userDao.emailCheck(signUpEmail);
	}

	@Override
	public int signUpPhoneCheck(SignUpVo signUpVo) {
		return userDao.phoneCheck(signUpVo);
	}

	@Override
	public int signUp(SignUpVo signUpVo) {
		return userDao.signUp(signUpVo);
	}

	@Override
	public int signIn(SignInVo signInVo) {
		return userDao.signIn(signInVo);
	}

	@Override
	public UserDto getUser(String user_email) {
		return userDao.getUser(user_email);
	}

	@Override
	public Cookie setUserCookie(String user_email) {
		Cookie cookie_email = new Cookie("user_email", user_email);
		cookie_email.setMaxAge(60*60*24);
		return cookie_email;
	}

}
