package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.service.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbUser;

@Controller
public class OrderController {

	@Value("${TT_CART}")
	private String TT_CART;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response) {
		// 1、接收表单提交的数据OrderInfo。
		// 2、补全用户信息。
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		Long userId = orderInfo.getUserId();
		if (userId == null) {
			return "success";
		}
		// 3、调用Service创建订单。
		TaotaoResult result = orderService.createOrder(orderInfo);
		// 取订单号
		String orderId = result.getData().toString();
		// a)需要Service返回订单号
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		// b)当前日期加三天。
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
		// 删除cookie中的订单信息
		List<TbOrderItem> list = orderInfo.getOrderItems();
		List<TbItem> cartList = getCartList(request);// 148362745694690
		for (TbOrderItem tbOrderItem : list) {
			Long itemId = new Long(tbOrderItem.getItemId());
			for (int i = 0; i < cartList.size(); i++) {
				TbItem tbItem = cartList.get(i);
				if (tbItem.getId().equals(itemId)) {
					cartList.remove(tbItem);
				}
			}
		}
		if (cartList.size() > 0) {
			System.out.println("新购物车cartList：" + cartList);
			// 将购物完成后的购物信息写回cookie
			CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList));
		} else {
			System.out.println("购物车已清空！");
			CookieUtils.deleteCookie(request, response, TT_CART);
		}
		// 4、返回逻辑视图展示成功页面
		return "success";
	}

	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		// 取用户id
		// 从cookie中取token，然后根据token查询用户信息。需要调用sso系统的服务。
		// 根据用户id查询收货地址列表
		// 从cookie中取商品列表
		List<TbItem> cartList = getCartList(request);
		// 传递给页面
		request.setAttribute("cartList", cartList);
		// 返回逻辑视图
		return "order-cart";
	}

	private List<TbItem> getCartList(HttpServletRequest request) {
		// 取购物车列表
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		// 判断json是否为null
		if (StringUtils.isNotBlank(json)) {
			// 把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}

}
