package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/**
 * 用户注册处理Service
 * @author Administrator
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;

	/**
	 * 校验用户注册信息
	 */
	@Override
	public E3Result checkData(String param, Integer type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//1:用户名,2:手机
		if(type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if(type == 2) {
			criteria.andPhoneEqualTo(param);
		} else {
			return E3Result.build(400, "校验类型错误");
		}
		
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		
		//判断结果中是否包含数据
		//如果有,返回false
		if(list != null && list.size() > 0) {
			return E3Result.ok(false);
		}
		//如果没有,返回true
		return E3Result.ok(true);
	}

	/**
	 * 注册
	 */
	@Override
	public E3Result regist(TbUser user) {
		//校验数据有效性
		if(user.getUsername().isEmpty() || user.getUsername() == null || 
				user.getPassword().isEmpty() || user.getPassword() == null || 
				user.getPhone().isEmpty() || user.getPhone() == null) {
			return E3Result.build(400, "用户数据不完整,注册失败");
		}
		//1:用户名,2:手机
		E3Result e3Result = checkData(user.getUsername(),1);
		if(!(boolean) e3Result.getData()) {
			return E3Result.build(400, "该用户名已被注册,注册失败");
		}
		e3Result = checkData(user.getPhone(),2);
		if(!(boolean) e3Result.getData()) {
			return E3Result.build(400, "该手机已被绑定,注册失败");
		}
		
		//补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		//对密码进行md5加密
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		
		//把用户数据插入到数据库中
		tbUserMapper.insert(user);
		
		//返回添加成功
		return e3Result.ok();
	}

}
