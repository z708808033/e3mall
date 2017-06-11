package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

/**
 * 内容分类管理
 * @author Administrator
 *
 */
public interface ContentCategoryService {
	/**
	 * 根据parentId获取内容分类
	 * @return
	 */
	public List<EasyUITreeNode> getContentCategory(Long parentId);
	
	/**
	 * 添加内容分类
	 * @param parentId
	 * @param name
	 * @return
	 */
	public E3Result addContentCategory(Long parentId,String name);
	
	/**
	 * 修改内容分类名称
	 * @param id
	 * @param name
	 */
	public void updateContentCategory(Long id,String name);
	
	/**
	 * 删除内容分类
	 * @param id
	 * @return
	 */
	public E3Result deleteContentCategory(Long id);
}
