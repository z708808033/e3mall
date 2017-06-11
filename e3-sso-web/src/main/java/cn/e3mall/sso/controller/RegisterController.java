package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册功能Controller
 * @author Administrator
 *
 */
@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	
	/**
	 * 跳转到注册页面 
	 * @return
	 */
	@RequestMapping("/page/register")
	public String showRegisterPage() {
		return "register";
	}
	
	/**
	 * 校验用户注册信息
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type) {
		return registerService.checkData(param, type);
	}
	
	/**
	 * 注册
	 * @param tbUser
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result regist(TbUser tbUser) {
		return registerService.regist(tbUser);
	}
}
