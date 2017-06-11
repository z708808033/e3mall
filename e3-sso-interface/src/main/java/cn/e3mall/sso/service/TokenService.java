package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * 根据token查询用户信息
 * @author Administrator
 *
 */
public interface TokenService {
	/**
	 * 根据token查询用户信息
	 * @param token
	 * @return
	 */
	public E3Result getUserByToken(String token);
}
