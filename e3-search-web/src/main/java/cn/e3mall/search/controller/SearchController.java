package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 商品搜索Controller
 * @author Administrator
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	/**
	 * 商品搜索
	 * @param keyword
	 * @param page
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
		SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",result.getTotalPages());
		model.addAttribute("recordCount",result.getRecordCount());
		model.addAttribute("itemList",result.getItemList());
		model.addAttribute("page",page);
		
		//异常测试
		//int a = 1/0;
		
		return "search";
	}
}
