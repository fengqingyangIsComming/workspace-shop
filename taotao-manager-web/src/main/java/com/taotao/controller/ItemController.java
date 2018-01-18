package com.taotao.controller;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 商品管理
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private Topic topicDestination;
	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult list = itemService.getItemList(page, rows);
		return list;
	}

	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc) {
		TaotaoResult taotaoResult = itemService.addItem(item, desc);
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("" + item.getId());
				System.out.println("发送消息:" + item.getId());
				return textMessage;
			}
		});
		return taotaoResult;
	}

	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoResult delItem(String ids) {
		return itemService.delItem(ids);
	}
	//下架
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public TaotaoResult instock(String ids) {
		return itemService.instock(ids, false);
	}
	//上架
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public TaotaoResult reshelf(String ids) {
		return itemService.instock(ids, true);
	}

	@RequestMapping("/item/query/desc/{itemid}")
	@ResponseBody
	public TaotaoResult queryItemDesc(@PathVariable("itemid") Long itemid) {
		return itemService.queryItemDesc(itemid);
	}

}
