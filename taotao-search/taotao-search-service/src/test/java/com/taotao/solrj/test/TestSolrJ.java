package com.taotao.solrj.test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
	@Test
	public void testSolrCloudAddDocument() throws Exception {
		//第一步：把solrJ相关的jar包添加到工程中。
		//第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		//参数是zookeeper的地址列表，使用逗号分隔
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.22:2181,192.168.25.22:2182,192.168.25.22:2183");
		// 第三步：需要设置DefaultCollection属性。
		solrServer.setDefaultCollection("collection2");
		// 第四步：创建一SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第五步：向文档对象中添加域
		document.addField("item_title", "测试商品");
		document.addField("item_price", "100");
		document.addField("id", "001");
		// 第六步：把文档对象写入索引库。
		solrServer.add(document);
		// 第七步：提交。
		solrServer.commit();

	}

	@Test
	public void testAddDocument() throws Exception {
		// 第一步：把solrJ的jar包添加到工程中。
		// 第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.126:8086/solr/");
		// 第三步：创建一个文档对象SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
		document.addField("id", "test001");
		document.addField("item_title", "测试商品");
		document.addField("item_price", "199");
		// 第五步：把文档添加到索引库中。
		solrServer.add(document);
		// 第六步：提交。
		solrServer.commit();

	}

	@Test
	public void deleteDocumentById() throws Exception {
		// 第一步：创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.126:8086/solr/");
		// 第二步：调用SolrServer对象的根据id删除的方法。
		solrServer.deleteById("1");
		// 第三步：提交。
		solrServer.commit();
	}

	@Test
	public void deleteDocumentByQuery() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.126:8086/solr/");
		solrServer.deleteByQuery("item_title:测试商品");
		solrServer.commit();
	}

}
