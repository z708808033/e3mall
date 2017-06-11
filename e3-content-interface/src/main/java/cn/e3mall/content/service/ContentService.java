package cn.e3mall.content.service;

import java.util.List;


import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author Administrator
 *
 */
public interface ContentService {
	/**
	 * 添加内容
	 * @param tbContent
	 * @return
	 */
	public E3Result addContent(TbContent tbContent);
	
	/**
	 * 根据内容分类id查询该分类下所有内容
	 * @param cid
	 * @return
	 */
	public List<TbContent> getContentListByCid(Long cid); 
	
	/**
	 * 查询内容
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows);
	
	/**
	 * 编辑内容
	 * @param tbContent
	 * @return
	 */
	public E3Result updateContent(TbContent tbContent);
	
	/**
	 * 删除内容
	 * @param tbContent
	 * @return
	 */
	public E3Result deleteContent(Long[] ids);
}
