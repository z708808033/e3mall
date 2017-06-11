package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登陆处理Service
 * @author Administrator
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	/**
	 * 用户登录
	 */
	@Override
	public E3Result userLogin(String username, String password) {
		if(username.isEmpty() || username == null || password.isEmpty() || password == null) {
			return E3Result.build(400, "用户名或密码不能为空");
		}
		//校验用户名和密码
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//校验用户名
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return E3Result.build(400, "用户名或密码错误");
		}
		
		//对密码进行加密
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		
		//校验密码
		TbUser user = list.get(0);
		if(!user.getPassword().equals(password)) {
			return E3Result.build(400, "用户名或密码错误");
		}
		
		//校验通过,利用UUID生成token
		String token = UUID.randomUUID().toString();
		
		//把token作为key,user的id作为值,存入redis缓存中
		//去掉user对象中的password属性
		user.setPassword(null);
		jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		
		return E3Result.ok(token);
	}

	/**
	 * 用户登出
	 */
	@Override
	public E3Result userLogout(String token) {
		jedisClient.del("SESSION:" + token);
		return E3Result.ok();
	}

}
