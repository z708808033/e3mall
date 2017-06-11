package cn.e3mall.order.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

/**
 * 订单服务Service
 * @author Administrator
 *
 */
public interface OrderService {
	/**
	 * 创建订单
	 * @param orderInfo
	 * @return
	 */
	public E3Result createOrder(OrderInfo orderInfo);
}
