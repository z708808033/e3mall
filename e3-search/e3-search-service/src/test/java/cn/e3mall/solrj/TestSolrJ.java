package cn.e3mall.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
//	@Test
//	public void fun1() throws SolrServerException, IOException {
//		//创建一个SolrServer对象,创建一个连接,参数是solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
//		
//		//创建一个文档对象SolrInputDocument
//		SolrInputDocument document = new SolrInputDocument();
//		
//		//向文档对象中添加域.文档中必须包含一个id域名,所有域的名称必须在schema.xml中定义
//		document.addField("id", "doc01");
//		document.addField("item_title", "测试商品1");
//		document.addField("item_price", 1000);
//		
//		//把文档写入索引库
//		solrServer.add(document);
//		
//		//提交
//		solrServer.commit();
//	}
//	
//	@Test
//	public void fun2() throws SolrServerException, IOException {
//		//创建一个SolrServer对象,创建一个连接,参数是solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
//		
////		solrServer.deleteById("doc01");
//		solrServer.deleteByQuery("id:doc01");
//		
//		//提交
//		solrServer.commit();
//	}
	
//	@Test
//	public void fun3() throws SolrServerException, IOException {
//		//创建一个SolrServer对象
//		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/collection1");
//		
//		//创建一个SolrQuery对象
//		SolrQuery solrQuery = new SolrQuery();
//		
//		//设置查询条件
//		solrQuery.set("q", "手机");
//		solrQuery.set("df","item_title");
//		solrQuery.setStart(0);
//		solrQuery.setRows(5);
//		solrQuery.setHighlight(true);
//		solrQuery.addHighlightField("item_title");
//		solrQuery.setHighlightSimplePost("<em color=red>");
//		solrQuery.setHighlightSimplePost("</em>");
//		
//		
//		//执行查询
//		QueryResponse queryResponse = solrServer.query(solrQuery);
//		
//		
//		//得到文档列表,取出查询结果的总记录数
//		SolrDocumentList solrDocumentList = queryResponse.getResults();
//		System.out.println("总记录数:" + solrDocumentList.getNumFound());
//		
//		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
//		//遍历文档列表,取域的内容
//		for (SolrDocument solrDocument : solrDocumentList) {
//			System.out.println(solrDocument.get("id"));
//			
//			//取高亮
//			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
//			if(list != null && list.size() > 0) {
//				System.out.println(list.get(0));
//			}else {
//				System.out.println(solrDocument.get("item_title"));
//			}
//			System.out.println(solrDocument.get("item_sell_point"));
//			System.out.println("----------");
//		}
//	}
	
}
