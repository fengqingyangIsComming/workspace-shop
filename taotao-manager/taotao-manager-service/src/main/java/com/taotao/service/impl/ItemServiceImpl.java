package com.taotao.service.impl;

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
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.service.ItemService;

/**
 * 商品服务
 * 
 * @Date 2016年12月29日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	// @Autowired
	// private JmsTemplate jmsTemplate;
	// @Autowired
	// private Topic topicDestination;

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		Date date = new Date();
		item.setUpdated(date);
		// 商品状态
		item.setStatus((byte) 1);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		long itemId;
		if (item.getId() != null) {// 更新操作
			itemId = item.getId();
			itemMapper.updateByPrimaryKeySelective(item);
			itemDesc.setItemId(itemId);
			itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		} else {// 添加操作
				// 使用Idutil生成商品id
			itemId = IDUtils.genItemId();
			item.setId(itemId);
			item.setCreated(date);
			itemMapper.insert(item);
			itemDesc.setItemId(itemId);
			itemDesc.setCreated(date);
			itemDescMapper.insert(itemDesc);
		}
		
		
		/*
		 * jmsTemplate.send(topicDestination, new MessageCreator() {
		 * 
		 * @Override public Message createMessage(Session session) throws
		 * JMSException { TextMessage textMessage = session.createTextMessage(""
		 * + itemId); return textMessage; } });
		 */
		// 问题：事务没提交，已经发送消息，出现空指针异常
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult delItem(String ids) {
		String[] split = ids.split(",");
		if (split.length == 1) {
			Long id = Long.valueOf(ids);
			itemDescMapper.deleteByPrimaryKey(id);
			itemMapper.deleteByPrimaryKey(id);
		} else {
			List<Long> values = new ArrayList<>();
			for (String str : split) {
				Long id = Long.valueOf(str);
				values.add(id);
			}
			TbItemDescExample tbItemDescExample = new TbItemDescExample();
			com.taotao.pojo.TbItemDescExample.Criteria tbItemDescCriteria = tbItemDescExample.createCriteria();
			tbItemDescCriteria.andItemIdIn(values);
			itemDescMapper.deleteByExample(tbItemDescExample);
			TbItemExample itemExample = new TbItemExample();
			com.taotao.pojo.TbItemExample.Criteria itemCriteria = itemExample.createCriteria();
			itemCriteria.andIdIn(values);
			itemMapper.deleteByExample(itemExample);
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult queryItemDesc(Long itemid) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemid);
		return TaotaoResult.ok(itemDesc);
	}

	@Override
	public TbItem getTbItem(long itemId) {
		try {
			// 查询缓存
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				// 把json转换为java对象
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				System.out.println("获取缓存成功" + json);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getTbItem缓存服务器异常！");
		}

		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		try {
			// 把数据保存到缓存
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
			// 设置缓存的有效期
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
			System.out.println("添加缓存成功" + JsonUtils.objectToJson(tbItem));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getTbItem缓存服务器异常！");
		}

		return tbItem;
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		try {
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
			// 判断缓存是否命中
			if (StringUtils.isNotBlank(json)) {
				// 转换为java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				System.out.println("获取缓存成功" + json);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getItemDescById缓存服务器异常！");
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
			System.out.println("添加缓存成功" + JsonUtils.objectToJson(itemDesc));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getItemDescById缓存服务器异常！");
		}
		return itemDesc;
	}

	// flag为true时上架，false时下架
	@Override
	public TaotaoResult instock(String ids, boolean flag) {
		String[] idArray = ids.split(",");
		List<Long> idList = new ArrayList<>();
		for (int i = 0; i < idArray.length; i++) {
			idList.add(new Long(idArray[i]));
		}

		TbItem record = new TbItem();
		// 1:正常，2：下架，3未知
		if (flag) {
			// true时上架
			record.setStatus((byte) 1);
		} else {
			//false时下架
			record.setStatus((byte) 2);
		}
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();

		criteria.andIdIn(idList);
		itemMapper.updateByExampleSelective(record, example);
		return TaotaoResult.ok();
	}

}
