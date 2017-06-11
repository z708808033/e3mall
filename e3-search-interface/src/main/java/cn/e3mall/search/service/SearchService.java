package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索Service
 * @author Administrator
 *
 */
public interface SearchService {
	/**
	 * 商品搜索
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(String keyword,Integer page,Integer rows) throws Exception; 
}
