package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

/**
 * 商品分类管理
 * @author Administrator
 *
 */

@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	
	/**
	 * 根据parentId查询商品分类
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//查询商品分类
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(example);
		
		//创建一个EasyUITreeNode集合
		List<EasyUITreeNode> easyUITreeNodeList = new ArrayList<EasyUITreeNode>();
		
		//为EasyUITreeNode集合添加元素
		for (TbItemCat tbItemCat : tbItemCatList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed" : "open");
			easyUITreeNodeList.add(easyUITreeNode);
		}
		return easyUITreeNodeList;
	}

}
