package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;


/**
 * 用户登陆处理Service
 * @author Administrator
 *
 */
public interface LoginService {
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public E3Result userLogin(String username,String password);
	
	/**
	 * 用户登出
	 * @param token
	 * @return
	 */
	public E3Result userLogout(String token);
}
