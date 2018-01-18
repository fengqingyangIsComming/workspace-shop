package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;

public interface OrderService {
	TaotaoResult createOrder(OrderInfo orderInfo) ;
}
