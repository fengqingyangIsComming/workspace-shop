package com.taotao.search.dao;

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

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {
		// 引用solr服务查询
		QueryResponse response = solrServer.query(query);
		// 获取查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument sdoc : solrDocumentList) {
			Object objectid = sdoc.get(SearchItem.ID);
			SearchItem item = new SearchItem();
			item.setId(Long.valueOf((String) objectid));
			item.setCategory_name((String) sdoc.get(SearchItem.CATEGORY_NAME));
			item.setImage((String) sdoc.get(SearchItem.IMAGE));
			item.setPrice((long) sdoc.get(SearchItem.PRICE));
			item.setSell_point((String) sdoc.get(SearchItem.SELL_POINT));
			// 取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			// 无高亮默认值
			String title = (String) sdoc.get(SearchItem.TITLE);
			if (highlighting.get(objectid) != null) {
				List<String> titles = highlighting.get(objectid).get(SearchItem.TITLE);
				// 有高亮显示的内容时。
				if (titles != null && titles.size() > 0) {
					title = titles.get(0);
				}
			}
			item.setTitle(title);
			itemList.add(item);
		}
		SearchResult result = new SearchResult();
		result.setItemList(itemList);
		// 获取总记录数
		result.setRecordCount(solrDocumentList.getNumFound());
		return result;
	}
}
