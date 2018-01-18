package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Value("${TT_TOKEN}")
	private String TT_TOKEN;
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// 执行Handler之前执行此方法
		// a)从cookie中取token。
		String token = CookieUtils.getCookieValue(request, TT_TOKEN);
		if (StringUtils.isBlank(token)) {
			// 取当前请求的url
			String url = request.getRequestURL().toString();
			// b)没有token，需要跳转到登录页面。
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			// 拦截
			return false;
		}
		// c)有token。调用sso系统的服务，根据token查询用户信息。
		TaotaoResult result = userService.getUserByToken(token);
		if (result.getStatus() != 200) {
			// d)如果查不到用户信息。用户登录已经过期。需要跳转到登录页面。
			// 取当前请求的url
			String url = request.getRequestURL().toString();
			// b)没有token，需要跳转到登录页面。
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			// 拦截
			return false;
		}
		TbUser user = (TbUser)result.getData();
		request.setAttribute("user", user);
		// e)查询到用户信息。放行。
		return true;
	}

}
