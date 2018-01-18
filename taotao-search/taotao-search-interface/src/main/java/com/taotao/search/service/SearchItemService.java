package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {
	TaotaoResult importAllItemIndex() throws Exception;
	TaotaoResult addDocument(long itemId)throws Exception;
}
