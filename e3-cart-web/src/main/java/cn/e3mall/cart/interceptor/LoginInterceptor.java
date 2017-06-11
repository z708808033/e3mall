package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, 
			Object handler, Exception exception)
			throws Exception {
		//返回ModelAndView之后
		//可以在此处理异常
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		//handler执行之后,返回ModelAndView之前
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		//执行handler之前执行此方法,返回true则放行,返回false则拦截
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		
		//如果没有token,则表示是未登录状态
		if(token == null || token.isEmpty()) {
			return true;
		}
		
		//如果有token,调用sso系统的服务,根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		
		//如果没有取到用户信息,则表示登陆过期,直接放行
		if(result.getStatus() != 200) {
			return true;
		}
		
		//如果取到用户信息,则表示是登陆状态
		TbUser user = (TbUser) result.getData();
		//把用户信息放到request中,只需要在Controller中判断request是否包含user信息.放行
		request.setAttribute("user", user);
		return true;
	}

}
