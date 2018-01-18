package com.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ParamService;

/**
 * 商品服务
 * 
 * @Date 2016年12月29日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
@Service
public class ParamServiceImpl implements ParamService {

	@Autowired
	private TbItemParamMapper paramMapper;

	@Override
	public EasyUIDataGridResult getParamList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemParamExample example = new TbItemParamExample();
		List<TbItemParam> list = paramMapper.selectByExample(example);
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

}
