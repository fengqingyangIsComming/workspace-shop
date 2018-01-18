package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

public class ItemListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = null;
			Long itemId = null;
			// 取商品id
			if (message instanceof TextMessage) {
				textMessage = (TextMessage) message;
				itemId = Long.parseLong(textMessage.getText());
			}
			// 向索引库添加文档
			//TaotaoResult taotaoResult = 
			searchItemService.addDocument(itemId);
			System.out.println("添加索引:"+itemId);
			//System.out.println(taotaoResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
