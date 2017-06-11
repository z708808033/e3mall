package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

/**
 * 订单管理Controller
 * @author Administrator
 *
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 跳转到提交订单页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		//取出用户id
		TbUser user = (TbUser) request.getAttribute("user");
		
		//根据用户id取收货地址
		//使用静态数据
		//取支付方式列表
		//使用静态数据
		
		//根据用户id取购物车列表
		List<TbItem> cartList = cartService.getCart(user.getId());
		for (TbItem tbItem : cartList) {
			String images = tbItem.getImage();
			if(images != null) {
				tbItem.setImage(images.split(",")[0]);
			}
		}
		request.setAttribute("cartList", cartList);
		
		return "order-cart";
	}
	
	/**
	 * 创建订单
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request) {
		//取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		
		//把用户信息添加到orderInfo中
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		//调用服务生成订单
		E3Result result = orderService.createOrder(orderInfo);
		
		//如果订单生成成功
		if(result.getStatus() == 200) {
			//删除购物车
			cartService.clearCartItem(user.getId());
		}
		
		//把订单号传递给页面
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		
		//返回逻辑视图
		return "success";
	}
}
