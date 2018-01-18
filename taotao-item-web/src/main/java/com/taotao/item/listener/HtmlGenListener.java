package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 创建商品静态页面
 * @Date 2017年1月6日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {
		try {
			// 业务逻辑：
			// 1、创建一个MessageListener接口的实现类
			// 2、从message中取商品id
			TextMessage textMessage = (TextMessage) message;
			// 3、查询商品基本消息、商品描述。
			String text = textMessage.getText();
			Long itemId = new Long(text);
			TbItem tbItem = itemService.getTbItem(itemId);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			// 创建数据集
			Map<String ,Object> data = new HashMap<>();
			data.put("item", tbItem);
			data.put("itemDesc", tbItemDesc);
			// 4、创建商品详情页面的模板。
			
			// 5、指定文件输出目录
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.htm");

			Writer out = new FileWriter(new File(HTML_OUT_PATH + itemId + ".html"));
			// 6、生成静态文件。
			template.process(data, out);
			out.close();
		} catch (Exception e) {

		}
	}

}
