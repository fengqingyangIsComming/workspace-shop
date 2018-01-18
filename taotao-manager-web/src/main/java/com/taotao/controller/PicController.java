package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

@Controller
public class PicController {

	@Value("${IMAGE_SERVER_URL}")
	public String IMAGE_SERVER_URL;

//	@RequestMapping(value="/pic/upload",produces="text/plain;charset=utf-8")
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			// 接收页面传递过来的文件内容
			byte[] fileContent = uploadFile.getBytes();
			// 获取文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			System.out.println(originalFilename);
			// 把文件内容上传到图片服务器
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			FastDFSClient client = new FastDFSClient("classpath:fast_dfs.conf");
			String url = client.uploadFile(fileContent, extName);
			// 从配置文件获取图片服务器的url
			// 创建返回结果对象
			result.put("error", 0);
			result.put("url", IMAGE_SERVER_URL + url);
			// 返回结果
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败");
		}
		System.out.println(result);
		return JsonUtils.objectToJson(result);
	}
}
