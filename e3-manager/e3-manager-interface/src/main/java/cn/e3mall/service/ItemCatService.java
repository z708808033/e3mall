package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;

/**
 * 商品分类服务Service
 * @author Administrator
 *
 */
public interface ItemCatService {
	/**
	 * 根据parentId查询商品分类
	 */
	public List<EasyUITreeNode> getItemCatList(Long parentId);
}
