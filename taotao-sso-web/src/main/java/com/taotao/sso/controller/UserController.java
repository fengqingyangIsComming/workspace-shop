package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;

	/**
	 * 验证用户是否可用
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
		return userService.checkData(param, type);
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		return userService.createUser(user);
	}

	@RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult result = userService.getUserByToken(token);
		String json = JsonUtils.objectToJson(result);
		if (StringUtils.isNotBlank(callback)) {
			String strResult = callback + "(" + json + ")";
			return strResult;
		}
		return json;
	}

	@RequestMapping("/user/logout/{token}")
	public String logout(@PathVariable String token) {
		userService.delUserByToken(token);
		return "redirect:http://localhost:8082/";
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、接收两个参数。
		// 2、调用Service进行登录。
		TaotaoResult result = userService.login(username, password);
		// 3、从返回结果中取token，写入cookie。Cookie要跨域。
		Object obj = result.getData();
		if (obj != null) {
			String token = obj.toString();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		// 4、响应数据。Json数据。TaotaoResult，其中包含Token。
		return result;

	}

}
