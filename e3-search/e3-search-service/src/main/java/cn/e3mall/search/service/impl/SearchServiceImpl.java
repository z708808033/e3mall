package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 商品搜索Service
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;

	/**
	 * 商品搜索
	 */
	@Override
	public SearchResult search(String keyword, Integer page, Integer rows) throws Exception {
		//创建SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		
		//设置查询条件
		solrQuery.set("q", keyword);
		
		//设置分页条件
		if(page <= 0 || page == null) {
			page = 1;
		}
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);
		
		//设置默认搜索域
		solrQuery.set("df", "item_title");
		
		//开启高亮并设置高亮属性
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		
		//执行查询
		SearchResult result = searchDao.search(solrQuery);
		
		//计算总页数
		Long recordCount = result.getRecordCount();
		Integer totalPages;
		if(recordCount % rows == 0) {
			totalPages = (int) (recordCount / rows);
		} else {
			totalPages = (int) (recordCount / rows + 1);
		}
		result.setTotalPages(totalPages);
		return result;
	}
}
