package com.springboot.kakao.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.kakao.model.beans.FileBean;
import com.springboot.kakao.service.NoticeService;
import com.springboot.kakao.service.UserService;
import com.springboot.kakao.model.dto.NoticeDto;
import com.springboot.kakao.model.dto.NoticeInsertDto;
import com.springboot.kakao.model.dto.UserDto;

@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/list/{page}")
	public ModelAndView noticeIndex(@PathVariable String page, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("user_email")) {
					HttpSession session = request.getSession();
					UserDto userDto = userService.getUser(c.getValue());
					session.setAttribute("login_user", userDto);
				}
			}
		}
		ModelAndView mav = new ModelAndView("notice/notice");
		mav.addObject("noticeList", noticeService.getNoticeList(noticeService.parseIntPageNumber(page)));
		mav.addObject("noticeBean", noticeService.getNoticeBean());
		return mav;
	}
	
	@GetMapping("/insert")
	public String noticeInsertIndex(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("login_user") == null) {
			return "redirect:/sign-in";
		}
		
		Date date = new Date();
		model.addAttribute("now", date);
		return "notice/notice_insert";
	}
	
	
	@GetMapping("/{code}")
	public String noticeDtlIndex(Model model, @PathVariable String code) {
		NoticeDto noticeDto = noticeService.getNotice(code);
		
		model.addAttribute("notice", noticeDto);
		model.addAttribute("fileList", noticeService.getFileList(noticeDto));
		return "notice/notice_dtl";
	}
	
	@PutMapping("/update/{code}")
	public String noticeUpdateIndex(Model model, @PathVariable String code) {
		NoticeDto noticeDto = noticeService.getNotice(code);
		model.addAttribute(code);
		return "notice/notice_update";
	}
	@DeleteMapping("/{code}")
	public String noticeDelete(Model model, @PathVariable String code) {
		System.out.println("되고 있음");
		int result = noticeService.noticeDelete(code);
		if(result == 2) {
			return "redirect:/notice/list/1";
		}
		return "redirect:/notice/" + code;
	}
}