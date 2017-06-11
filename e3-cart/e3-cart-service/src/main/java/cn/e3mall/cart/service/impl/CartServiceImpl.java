package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 登陆状态下的购物车服务Service
 */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	/**
	 * 登陆状态下往购物车添加商品
	 */
	@Override
	public E3Result addCart(Long userId, Long itemId,Integer num) {
		//向redis中添加购物车,数据类型是hash.key:用户id field:商品id value:商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		
		//如果存在,则相加
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		
		//如果不存在,则根据商品id取商品信息,并添加至购物车列表
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String images = item.getImage();
		if(images != null && !images.isEmpty()) {
			item.setImage(images.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	/**
	 * 合并redis和cookie中的商品
	 */
	@Override
	public E3Result mergeCart(Long userId, List<TbItem> itemList) {
		//遍历cookie中的所有商品
		for (TbItem tbItem : itemList) {
			//合并商品
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	/**
	 * 展示redis中的购物车列表
	 */
	@Override
	public List<TbItem> getCart(Long userId) {
		//根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		for (String json : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	/**
	 * 登陆状态下更新商品数量
	 */
	@Override
	public E3Result updateCartNumber(Long userId, Long itemId, Integer num) {
		//从redis中取出目标商品
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		
		//修改商品数量
		item.setNum(num);
		
		//把商品写回redis中
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	/**
	 * 登陆状态下删除商品
	 */
	@Override
	public E3Result deleteCartItem(Long userId, Long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCartItem(Long userId) {
		//删除购物车信息
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}

}
