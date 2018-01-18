package com.taotao.service.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taotao.content.jedis.JedisClient;

public class ItemChangeListener implements MessageListener {
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Autowired
	private JedisClient jedisClient;

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
			// 删除缓存 设置过期时间为-2
			// jedisClient.hdel(key, field)
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", 0);
			// 设置缓存的有效期
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", 0);
			System.out.println("删除缓存:" + ITEM_INFO_PRE + ":" + itemId + ":DESC,BASE");
			
			Long ttlDESC = jedisClient.ttl(ITEM_INFO_PRE + ":" + itemId + ":DESC");
			Long ttlBASE = jedisClient.ttl(ITEM_INFO_PRE + ":" + itemId + ":BASE");
			System.out.println("剩余时间ttlDESC:" + ttlDESC + ",ttlBASE" + ttlBASE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
