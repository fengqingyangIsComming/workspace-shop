package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String q, int page, int rows) throws Exception;
}
