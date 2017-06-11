package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.sso.service.LoginService;

/**
 * 用户登录处理Controller
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	/**
	 * 跳转到登陆页面 ,如果有附带的url,则跳转到指定的url
	 * @return
	 */
	@RequestMapping("/page/login")
	public String showLoginPage(String redirect,Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	/**
	 * 用户登陆
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result userLogin(String username,String password,HttpServletRequest request,
			HttpServletResponse response) {
		E3Result e3Result = loginService.userLogin(username, password);
		//判断是否登陆成功
		if(e3Result.getStatus() == 200) {
			//如果登陆成功,则把token写入cookie中
			String token = e3Result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		
		return e3Result;
	}
	
	@RequestMapping("/user/logout/{token}")
	public String userLogout(@PathVariable String token) {
		loginService.userLogout(token);
		return "redirect:http://localhost:8083";
	}
}
