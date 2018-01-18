package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		// 跟据商品id查询商品信息
		TbItem tbItem = itemService.getTbItem(itemId);
		// 把TbItem转换成Item对象
		// 根据商品id查询商品描述
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		// 把数据传递给页面
		model.addAttribute("item", tbItem);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
