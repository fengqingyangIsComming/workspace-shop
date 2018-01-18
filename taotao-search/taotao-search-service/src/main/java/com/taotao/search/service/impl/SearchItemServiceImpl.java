package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

/**
 * 导入所有数据到索引库
 * 
 * @Date 2017年1月2日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchItemMapper searchItemMapper;

	@Override
	public TaotaoResult importAllItemIndex() throws Exception {
		// 1、查询所有商品数据。
		List<SearchItem> itemList = searchItemMapper.getItemList();
		// 2、创建一个SolrServer对象。
		for (SearchItem searchItem : itemList) {
			// 3、为每个商品创建一个SolrInputDocument对象。
			SolrInputDocument document = new SolrInputDocument();
			// 4、为文档添加 域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			solrServer.add(document);
		}
		// 5、向索引库中添加文档。
		solrServer.commit();
		// 6、返回TaotaoResult。
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult addDocument(long itemId) throws Exception {
		SearchItem searchItem = searchItemMapper.getItemById(itemId);
		SolrInputDocument document = new SolrInputDocument();
		// 4、为文档添加 域
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		document.addField("item_desc", searchItem.getItem_desc());
		solrServer.add(document);
		// 5、向索引库中添加文档。
		solrServer.commit();
		// 6、返回TaotaoResult。
		return TaotaoResult.ok();
	}

}
