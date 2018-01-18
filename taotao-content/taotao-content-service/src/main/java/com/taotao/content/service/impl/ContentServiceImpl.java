package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

/**
 * 内容管理服务
 * 
 * @Date 2016年12月31日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public void saveContent(TbContent content) {
		long start = System.currentTimeMillis();
		Date date = new Date();
		content.setUpdated(date);
		if (content.getId() == null) {
			content.setCreated(date);
			contentMapper.insert(content);
		} else {
			contentMapper.updateByPrimaryKey(content);
		}
		
		try {
			// 同步删除缓存 redis 4条   mysql 4条
			jedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString());
			long end = System.currentTimeMillis();
			System.out.println("同步saveContent删除缓存，耗时" + (end - start) + "毫秒");
		} catch (Exception e) {
			
			// 发生异常时发出异常通知信息，如邮件通知管理员
			System.out.println("redis服务器异常");
		}
	}

	/**
	 * 向服务中添加缓存不能影响现有的业务逻辑
	 * 
	 * @param categoryId
	 * @return
	 * @see com.taotao.content.service.ContentService#getContentList(long)
	 */
	@Override
	public List<TbContent> getContentList(long categoryId) {
		long start = System.currentTimeMillis();
		// 查询缓存是否存在
		try {
			String json = jedisClient.hget(CONTENT_KEY, categoryId + "");

			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				long end = System.currentTimeMillis();
				System.out.println("查询缓存成功，耗时" + (end - start) + "毫秒");
				return list;
			}
		} catch (Exception e) {
			// 发生异常时发出异常通知信息，如邮件通知管理员
			System.out.println("redis服务器异常");
			
//			throw e;
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 添加缓存
		try {
			jedisClient.hset(CONTENT_KEY, categoryId + "", JsonUtils.objectToJson(list));
			long end = System.currentTimeMillis();
			System.out.println("缓存添加成功，耗时" + (end - start) + "毫秒");
		} catch (Exception e) {
			// 发生异常时发出异常通知信息，如邮件通知管理员
			System.out.println("redis服务器异常");
		}
		return list;
	}

	@Override
	public void deleteContent(String ids) {
		long start = System.currentTimeMillis();
		String[] split = ids.split(",");
		List<Long> values = new ArrayList<>();
		if (split != null && split.length > 0) {
			List<String> idList = new ArrayList<>();
			for (String sid : split) {
				Long id = Long.valueOf(sid);
				values.add(id);
				TbContent content = contentMapper.selectByPrimaryKey(id);
				idList.add(content.getCategoryId().toString());
			}
			try {
				// 同步删除缓存
				jedisClient.hdel(CONTENT_KEY, idList.toArray(new String[0]));
				long end = System.currentTimeMillis();
				System.out.println("被删除的id"+idList);
				System.out.println("同步deleteContent删除缓存，耗时" + (end - start) + "毫秒");
			} catch (Exception e) {
				// 发生异常时发出异常通知信息，如邮件通知管理员
				System.out.println("redis服务器异常");
			}
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(values);
		contentMapper.deleteByExample(example);

	}

}
