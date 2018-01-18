package com.taotao.order.job;

import java.util.Date;

public class OrderJob {
	// 任务执行方法
	public void execute() {
		System.out.println("任务已经执行！" + new Date());
	}
}
