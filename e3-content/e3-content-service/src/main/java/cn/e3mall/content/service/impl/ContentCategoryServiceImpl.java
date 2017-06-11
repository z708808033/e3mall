package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;

	/**
	 * 根据parentId获取内容分类
	 */
	@Override
	public List<EasyUITreeNode> getContentCategory(Long parentId) {
		//查询分类,得到分类集合
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentCategoryList = tbContentCategoryMapper.selectByExample(example);
		
		//创建一个EasyUITreeNode集合
		List<EasyUITreeNode> easyUITreeNodeList = new ArrayList<EasyUITreeNode>();
		
		//遍历分类集合,并为EasyUITreeNode集合添加元素
		for (TbContentCategory tbContentCategory : contentCategoryList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbContentCategory.getId());
			easyUITreeNode.setState(tbContentCategory.getIsParent() ? "closed":"open");
			easyUITreeNode.setText(tbContentCategory.getName());
			easyUITreeNodeList.add(easyUITreeNode);
		}
		return easyUITreeNodeList;
	}

	/**
	 * 添加内容分类
	 */
	@Override
	public E3Result addContentCategory(Long parentId, String name) {
		//创建一个TbContentCategory对象并补全属性,由于TbContentCategory的id是自增长的,所以不需要补id
		TbContentCategory tbContentCategory = new TbContentCategory();
		//新添加的节点一定是叶子节点
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		//默认排序是1
		tbContentCategory.setSortOrder(1);
		//1.正常,2.删除
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		
		//往数据库添加该TbContentCategory对象
		tbContentCategoryMapper.insert(tbContentCategory);
		
		//判断父节点的isparent属性,如果是false则改为true
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(parent.getIsParent() == false) {
			parent.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		
		//返回一个E3Result对象
		return E3Result.ok(tbContentCategory);
	}

	/**
	 * 修改内容分类名称
	 */
	@Override
	public void updateContentCategory(Long id, String name) {
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
	}

	/**
	 * 删除内容分类
	 */
	@Override
	public E3Result deleteContentCategory(Long id) {
		//获取该id对应的内容分类
		TbContentCategory son = tbContentCategoryMapper.selectByPrimaryKey(id);
		
		//判断是否为叶子节点,如果不是,则不允许删除
		if(son.getIsParent() == true) {
			E3Result e3Result = new E3Result();
			e3Result.setMsg("该分类不是叶子节点,不能删除");
			return e3Result;
		}
		
		//如果是叶子节点,则删除该节点
		tbContentCategoryMapper.deleteByPrimaryKey(id);
		
		//获取该节点所在父节点下方的所有子节点
		Long parentId = son.getParentId();
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> sonList = tbContentCategoryMapper.selectByExample(example);
		
		//判断该节点的父节点下方是否还有子节点,如果否,则将该父节点的isparent属性改为false
		if(sonList.size() <= 0) {
			TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok();
	}

	
}
