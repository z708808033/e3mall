package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

/**
 * 登陆状态下的购物车服务Service
 */
public interface CartService {
	/**
	 * 登陆状态下往购物车添加商品
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public E3Result addCart(Long userId,Long itemId,Integer num);
	
	/**
	 * 合并redis和cookie中的商品
	 * @param userId
	 * @param itemList
	 * @return
	 */
	public E3Result mergeCart(Long userId,List<TbItem> itemList);
	
	/**
	 * 展示redis中的购物车列表
	 * @param userId
	 * @return
	 */
	public List<TbItem> getCart(Long userId);
	
	/**
	 * 登陆状态下更新商品数量
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public E3Result updateCartNumber(Long userId,Long itemId,Integer num);
	
	/**
	 * 登陆状态下删除商品
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public E3Result deleteCartItem(Long userId,Long itemId);
	
	/**
	 * 删除购物车内所有商品
	 * @param userId
	 * @return
	 */
	public E3Result clearCartItem(Long userId);
}
