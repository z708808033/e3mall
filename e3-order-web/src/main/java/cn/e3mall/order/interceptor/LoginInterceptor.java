package cn.e3mall.order.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	@Value("${SSO_URL}")
	private String SSO_URL;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		
		//判断token是否存在
		if(token.isEmpty() || token == null) {
			//如果token不存在,表示未登录,跳转到登陆页面,用户登录成功后,跳转到当前请求的url
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			
			//拦截
			return false;
		}
		
		//如果token存在
		//根据token取用户信息
		
		E3Result result = tokenService.getUserByToken(token);
		
		//如果取不到,则用户登录已过期,需要登陆
		if(result.getStatus() != 200) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			
			//拦截
			return false;
		}
		
		//如果取到用户信息,则表示是登陆状态,把用户信息写入request中
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		
		//判断cookie中是否有购物车数据,如果有则合并到服务端
		String cartListJson = CookieUtils.getCookieValue(request, "cart",true);
		if(cartListJson != null && !cartListJson.isEmpty()) {
			List<TbItem> itemList = JsonUtils.jsonToList(cartListJson, TbItem.class);
			cartService.mergeCart(user.getId(), itemList);
		}
		
		//放行
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
