package com.taotao.mybatis.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	@Test
	public void testpagehelper(){
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-dao.xml");
		TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
		TbItemExample example=new TbItemExample();
		PageHelper.startPage(1, 30);
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		int pages = pageInfo.getPages();
		List<TbItem> list2 = pageInfo.getList();
		System.out.println(total+"_"+pages+"_"+list2);
	}

}
