package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList( long categoryId,Integer page, Integer rows) {
		return contentService.getContentList(categoryId,page,rows);
	}
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult saveContent(TbContent content) {
		contentService.saveContent(content);
		return TaotaoResult.ok();
	}
	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult deleteContent(String ids) {
		contentService.deleteContent(ids);
		return TaotaoResult.ok();
	}
}
