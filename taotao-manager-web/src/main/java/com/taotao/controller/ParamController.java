package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.service.ParamService;

@Controller
public class ParamController {

	@Autowired
	private ParamService paramService;

	@RequestMapping("/item/param/list")
	@ResponseBody
	public EasyUIDataGridResult paramList(Integer page, Integer rows) {
		EasyUIDataGridResult paramList = paramService.getParamList(page, rows);
		return paramList;
	}
}
