package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * 用户注册处理Service
 * @author Administrator
 *
 */
public interface RegisterService {
	/**
	 * 校验用户注册信息
	 * @param param
	 * @param type
	 * @return
	 */
	public E3Result checkData(String param,Integer type);
	
	/**
	 * 注册
	 * @param tbUser
	 * @return
	 */
	public E3Result regist(TbUser tbUser);
}
