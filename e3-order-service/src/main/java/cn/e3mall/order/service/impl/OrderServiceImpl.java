package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	
	/**
	 * 创建订单
	 */
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		//生成订单号.使用redis的incr生成
		//赋予订单号一个初始值
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY) + "";
		
		//补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		
		//插入订单表
		tbOrderMapper.insert(orderInfo);
		
		//向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成订单明细id
			String orderItemId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY) + "";
			
			//补全订单明细属性
			tbOrderItem.setId(orderItemId);
			tbOrderItem.setOrderId(orderId);
			
			//插入明细表
			tbOrderItemMapper.insert(tbOrderItem);
		}
		
		//向订单物流表插入数据
		TbOrderShipping tbOrderShipping = orderInfo.getOrderShipping();
		
		//补全属性
		tbOrderShipping.setOrderId(orderId);
		tbOrderShipping.setCreated(new Date());
		tbOrderShipping.setUpdated(new Date());
		
		//插入物流表
		tbOrderShippingMapper.insert(tbOrderShipping);
		
		//返回E3Result,包含订单号
		return E3Result.ok(orderId);
	}

}
