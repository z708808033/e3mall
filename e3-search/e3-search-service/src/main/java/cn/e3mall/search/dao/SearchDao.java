package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.mapper.TbItemMapper;

/**
 * 商品搜索dao
 * @author Administrator
 *
 */
@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private TbItemMapper tbItemMapper;
	
	/**
	 * 搜索商品
	 * @param solrQuery
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		//执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		
		//得到文档列表,取出查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		Long recordCount = solrDocumentList.getNumFound();
		
		//创建一个SearchResult对象
		SearchResult searchResult = new SearchResult();
		
		//设置总记录数
		searchResult.setRecordCount(recordCount);
		
		//遍历文档列表,取域的内容,并封装到itemList中
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : solrDocumentList) {
			//判断商品状态是否为上架状态
			Long id = Long.parseLong((String) solrDocument.get("id"));
			if(tbItemMapper.selectByPrimaryKey(id) == null ||tbItemMapper.selectByPrimaryKey(id).getStatus() != 1) {
				continue;
			}
			
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			
			//取标题高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if(list != null && list.size() > 0) {
				searchItem.setTitle(list.get(0));
			}else {
				searchItem.setTitle((String) solrDocument.get("item_title"));
			}
			itemList.add(searchItem);
		}
		searchResult.setItemList(itemList);
		
		//返回结果
		return searchResult;
	}
}
