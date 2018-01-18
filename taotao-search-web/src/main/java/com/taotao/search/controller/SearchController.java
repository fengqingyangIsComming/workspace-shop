package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {

	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;
	@Autowired
	private SearchService searchService ;
	@RequestMapping("/search")
	public String search(String q,@RequestParam(defaultValue="1")int page,Model model)throws Exception{
		q = new String(q.getBytes("iso8859-1"),"utf-8");
		SearchResult search = searchService.search(q, page, ITEM_ROWS);
		//回显查询条件
		model.addAttribute("query", q);
		//回显当前页
		model.addAttribute("page", page);
		//回显总页数
		model.addAttribute("totalPages", search.getPageCount());
		//查询结果列表
		model.addAttribute("itemList", search.getItemList());
		//int i = 1/0;
		//返回逻辑视图
		return "search";
	}
}
