package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车处理Controller
 * @author Administrator
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	/**
	 * 添加商品到购物车
	 * @param itemId
	 * @param number
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断是否是登陆状态
		//如果是登陆状态,则把购物车信息写入redis中
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		
		//如果不是,则把购物车信息写入cookie中
		//从cookie中取购物车列表
		List<TbItem> itemList = getCartListFromCookie(request);
		
		//判断商品在商品列表中是否存在
		boolean flag = false;
		for (TbItem tbItem : itemList) {
			//如果存在,则数量相加
			if(tbItem.getId().longValue() == itemId.longValue()) {
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				break;
			}
		}
		
		//如果不存在
		if(!flag) {
			//根据商品id查询商品信息
			TbItem item = itemService.getItemById(itemId);
			//设置商品数量
			item.setNum(num);
			//取一张图片
			String images = item.getImage();
			if(!images.isEmpty() || images != null) {
				item.setImage(images.split(",")[0]);
			}
			//把商品添加到商品列表
			itemList.add(item);
		}
		
		//将商品列表写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList),COOKIE_CART_EXPIRE, true);
		
		//返回添加成功页面
		return "cartSuccess";
	}
	
	/**
	 * 展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response) {
		//从cookie中取出购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		
		//判断用户是否登陆
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			//如果已经登陆
			cartService.mergeCart(user.getId(), cartList);
			
			//删除cookie
			CookieUtils.deleteCookie(request, response, "cart");
			
			//从服务端取购物车列表
			cartList = cartService.getCart(user.getId());
			
		}
		
		//如果没有登陆
		//把列表传递给页面
		request.setAttribute("cartList",cartList);
		
		//返回结果页面
		return "cart";
	}
	
	/**
	 * 更新购物车商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断是否为登陆状态
		TbUser user = (TbUser) request.getAttribute("user");
		
		//如果是
		if(user != null) {
			//调用登陆状态下的更新商品数量方法
			return cartService.updateCartNumber(user.getId(), itemId, num);
		}
		
		//如果不是
		//从cookie中取购物车列表
		List<TbItem> itemList = getCartListFromCookie(request);
		
		//遍历商品列表,找到对应的商品
		for (TbItem tbItem : itemList) {
			if(tbItem.getId().longValue() == itemId.longValue()) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), COOKIE_CART_EXPIRE, true);
		return E3Result.ok();
	}
	
	/**
	 * 删除购物车商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response) {
		//判断是否为登陆状态
		TbUser user = (TbUser) request.getAttribute("user");
		
		//如果是
		if(user != null) {
			//调用登陆状态下的删除商品方法
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		//从cookie中取出商品列表
		List<TbItem> itemList = getCartListFromCookie(request);
		
		//遍历商品列表
		for (TbItem tbItem : itemList) {
			//找到对应商品
			if(tbItem.getId().longValue() == itemId.longValue()) {
				//删除商品
				itemList.remove(tbItem);
				break;
			}
		}
		
		//写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
	
	/**
	 * 从cookie中取购物车商品列表
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		//从cookie中获取购物车商品列表
		String json = CookieUtils.getCookieValue(request, "cart", true);
		
		//判断json是否为空
		//如果为空,则返回一个ArrayList对象
		if(json == null || json.isEmpty()) {
			return new ArrayList();
		}
		
		//如果不为空,则把json转换成商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
}
