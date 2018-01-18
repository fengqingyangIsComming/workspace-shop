package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {

	// Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception e) {
		// 写日志文件
		// logger.error("系统发生异常", ex);
		System.out.println("系统发生异常" + e.getMessage());
		// 发邮件、发短信
		// Jmail：可以查找相关的资料
		// 需要在购买短信。调用第三方接口即可。
		// 展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "您的网络有问题，请稍后重试");
		modelAndView.setViewName("error/exception");
		return modelAndView;

	}

}
