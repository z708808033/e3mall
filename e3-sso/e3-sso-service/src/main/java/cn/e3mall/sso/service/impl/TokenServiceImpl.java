package cn.e3mall.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 根据token查询用户信息
 * @author Administrator
 *
 */
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	/**
	 * 根据token查询用户信息
	 */
	@Override
	public E3Result getUserByToken(String token) {
		//根据token到redis中取用户信息
		String json = jedisClient.get("SESSION:" + token);
		
		//如果取不到用户信息,说明登陆已经过期,返回登陆过期信息
		if(json == null || json.isEmpty()) {
			return E3Result.build(201, "用户登录已经过期");
		}
		
		//如果取到用户信息,则更新token的过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		//返回结果
		return E3Result.ok(user);
	}

}
