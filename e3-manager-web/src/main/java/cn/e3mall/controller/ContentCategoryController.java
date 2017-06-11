package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;


/**
 * 内容分类管理Controller
 * @author Administrator
 *
 */

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 根据parentId获得内容分类集合
	 * @param parentId
	 * @return
	 */
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name="id",defaultValue="0") Long parentId) {
		return contentCategoryService.getContentCategory(parentId);
	}
	
	/**
	 * 添加内容分类
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping(value="content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCategory(Long parentId, String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}
	
	/**
	 * 修改内容分类名称
	 * @param id
	 * @param name
	 */
	@RequestMapping("/content/category/update")
	public void updateContentCategory(Long id,String name) {
		contentCategoryService.updateContentCategory(id, name);
	}
	
	/**
	 * 删除内容分类
	 * @param id
	 * @return
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCategory(Long id) {
		return contentCategoryService.deleteContentCategory(id);
	}
}
