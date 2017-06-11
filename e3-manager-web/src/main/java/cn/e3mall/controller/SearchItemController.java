package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * solr索引库Controller
 * @author Administrator
 *
 */
@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	
	
	/**
	 * 导入solr索引库
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemList() {
		return searchItemService.importAllItems();
	}
}
