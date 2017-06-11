package cn.e3mall.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
//	@Test
//	public void fun1() throws Exception {
//		//zkHost:zookeeper的地址列表
//		String zkHost = "192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183";
//		
//		//创建一个集群的连接,应该使用CloudSolrServer创建
//		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
//		
//		//设置一个defaultCollection属性
//		String collection = "collection2";
//		cloudSolrServer.setDefaultCollection(collection);
//		
//		//创建一个文档对象
//		SolrInputDocument document = new SolrInputDocument();
//		
//		//向文档中添加域
//		document.setField("id", "solrCloud01");
//		document.setField("item_title", "solrCloudTest");
//		
//		//把文档写入索引库
//		cloudSolrServer.add(document);
//		
//		//提交
//		cloudSolrServer.commit();
//	}
//	
//	@Test
//	public void fun2() throws Exception {
//		String zkHost = "192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183";
//		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
//		String collection = "collection2";
//		cloudSolrServer.setDefaultCollection(collection);
//		
//		SolrQuery query = new SolrQuery();
//		query.set("q", "solrCloud01");
//		query.set("df", "id");
//		QueryResponse queryResponse = cloudSolrServer.query(query);
//		SolrDocumentList results = queryResponse.getResults();
//		System.out.println(results.getNumFound());
//		for (SolrDocument solrDocument : results) {
//			System.out.println(solrDocument.get("item_title"));
//		}
//	}
}
