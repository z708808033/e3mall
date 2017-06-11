package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理Controller
 * @author Administrator
 *
 */
@Controller
public class ContentController {
	@Autowired
	private ContentService contentSerivce;
	
	/**
	 * 添加内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent tbContent) {
		return contentSerivce.addContent(tbContent);
	}
	
	/**
	 * 查询内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(@RequestParam(defaultValue = "0")Long categoryId,
			@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "20")Integer rows) {
		return contentSerivce.getContentList(categoryId, page, rows);
	}
	
	/**
	 * 编辑内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3Result updateContent(TbContent tbContent) {
		return contentSerivce.updateContent(tbContent);
	}
	
	/**
	 * 删除内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(Long[] ids) {
		return contentSerivce.deleteContent(ids);
	}
	
}
