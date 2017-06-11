package cn.e3mall.search.service;

import cn.e3mall.common.pojo.E3Result;


/**
 * 索引库维护Service
 * @author Administrator
 *
 */
public interface SearchItemService {
	/**
	 * 导入索引库
	 * @return
	 */
	public E3Result importAllItems();
}
