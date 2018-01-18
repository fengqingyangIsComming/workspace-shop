package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(defaultValue = "0") long id) {
		return contentCategoryService.getContentCatList(id);
	}

	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult insertContentCat(Long parentId, String name) {
		return contentCategoryService.insertContentCat(parentId, name);
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCat(Long id, String name) {
		return contentCategoryService.updateContentCat(id, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCat(Long id) {
		return contentCategoryService.deleteContentCat(id);
	}

}
